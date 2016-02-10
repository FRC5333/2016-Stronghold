#include "splinelib.h"
#include "socket.h"
#include "sleep.h"
#include "conversions.h"

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define HOST_ADDRESS "localhost"
#define HOST_PORT 5802

int main() {
    setbuf(stdout, NULL);
    
    socket_init();
    int return_code = -1;
    
    printf("Acquiring Host Target at %s:%i", HOST_ADDRESS, HOST_PORT);
    char HOST_IP[50];
    while (return_code != 0) {
        return_code = socket_host(HOST_ADDRESS, HOST_IP);
        if (return_code != 0) {
            printf(".");
            sleep_ms(100);
        }
    }
    return_code = -1;
    
    printf("\nConnecting to Host Target at %s", HOST_IP);
    SOCKET host_socket = socket_create();
    while (return_code < 0) {
        return_code = socket_connect(host_socket, HOST_IP, HOST_PORT);
        if (return_code != 0) {
            printf(".");
            sleep_ms(100);
        }
    }
    printf("\nHost Connected!\n");
    char *id = "CP_SPL";
    
    send(host_socket, id, 6, 0);
    
    while (1) {
        char buf[4];
        read(host_socket, buf, 4);
        
        int buffer_read = bytesToInt(buf);
        if (buffer_read == 0xAB) {	     // Negotiation Byte 
            read(host_socket, buf, 4);
            double max_velocity = (double) bytesToFloat(buf);
            
            read(host_socket, buf, 4);
            double max_acceleration = (double) bytesToFloat(buf);
            
            read(host_socket, buf, 4);
            int waypoint_num = bytesToInt(buf);
            
            Waypoint waypoints[waypoint_num];
            
            int i = 0;
            for (i = 0; i < waypoint_num; i++) {
                read(host_socket, buf, 4);
                double x = (double) bytesToFloat(buf);
                
                read(host_socket, buf, 4);
                double y = (double) bytesToFloat(buf);
                
                read(host_socket, buf, 4);
                double angle = (double) bytesToFloat(buf);
                
                Waypoint wp = { x, y, angle };
                waypoints[i] = wp;
            }
            
            TrajectoryCandidate candidate;
            trajectory_prepare_candidate(waypoints, waypoint_num, FIT_HERMITE_CUBIC, SPLINE_SAMPLES_LO, 0.01, max_velocity, max_acceleration, 60.0, &candidate);
                
            int length = candidate.length;

            Segment seg[length];
            trajectory_generate(&candidate, &seg);
            
            intToBytes(0xBA, buf);
            send(host_socket, buf, 4, 0);
            
            intToBytes(length, buf);
            send(host_socket, buf, 4, 0);
            
            FILE *f = fopen("__lock.csv", "w");
            fprintf(f, "x,y\n");     // This line is required, but I'm not sure why.  
            
            for (i = 0; i < length; i++) {
                Segment s = seg[i];
                floatToBytes(s.x, buf);
                send(host_socket, buf, 4, 0);
                
                floatToBytes(s.y, buf);
                send(host_socket, buf, 4, 0);
                
                floatToBytes(s.position, buf);
                send(host_socket, buf, 4, 0);
                
                floatToBytes(s.velocity, buf);
                send(host_socket, buf, 4, 0);
                
                floatToBytes(s.acceleration, buf);
                send(host_socket, buf, 4, 0);
                
                floatToBytes(s.jerk, buf);
                send(host_socket, buf, 4, 0);
                
                floatToBytes(s.heading, buf);
                send(host_socket, buf, 4, 0);
           }
           
           fclose(f);
        }     
    }
    
    socket_quit();
    return 0;
}