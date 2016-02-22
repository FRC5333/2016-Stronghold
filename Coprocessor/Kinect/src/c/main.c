#include "kinect.h"
#include "socket.h"
#include "sleep.h"
#include "kinect_cv.hpp"
#include "conversions.h"
#include "driver_station.hpp"
#include "main.h"

#include <pthread.h>
#include <stdio.h>

#define HOST_ADDR_1 "roborio-5333-frc.local"
#define HOST_ADDR_2 "roborio-5333-frc.lan"
#define HOST_PORT 5802

SOCKET host_socket;

void send_to_rio(char *data, int length) {
    if (send(host_socket, data, length, 0) < 0) exit(1);
}

int main() {
    setbuf(stdout, NULL);
    
    socket_init();
    int return_code = -1;
    
    char HOST_IP[50];
    bzero(HOST_IP, 50);
    printf("Acquiring Host Target at %s:%i\n", HOST_ADDR_1, HOST_PORT);
    return_code = hostname_to_ip(HOST_ADDR_1, HOST_IP);
    if (return_code != 0) {
        printf("Acquiring Host Target at %s:%i\n", HOST_ADDR_2, HOST_PORT);
        return_code = hostname_to_ip(HOST_ADDR_2, HOST_IP);
        if (return_code != 0) {
            printf("Failed, using Static IP\n");
            memcpy(HOST_IP, "10.53.33.83", 11);
        }
    }
    return_code = -1;
    
    printf("Connecting to Host Target at %s\n", HOST_IP);
    host_socket = socket_create();
    return_code = socket_connect(host_socket, HOST_IP, HOST_PORT);
    if (return_code != 0) {
        printf("Failed\n");
        return 1;
    }
    printf("\nHost Connected!\n");
    char *id = "CP_KIN";
    
    send(host_socket, id, 6, 0);
    
    init_cv();
    init_kinect();
    kinect_video_ir();
    printf("Kinect Ready\n");
    init_ds();
    start_kinect();
    
    socket_quit();
    return 0;
}