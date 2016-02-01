#include "kinect.h"
#include "socket.h"
#include "sleep.h"
#include "kinect_cv.hpp"

#include <stdio.h>

#define HOST_ADDRESS "localhost"
#define HOST_PORT 5808

int main() {
    setbuf(stdout, NULL);
    
    // socket_init();
    // int return_code = -1;
    
    // printf("Acquiring Host Target at %s:%i", HOST_ADDRESS, HOST_PORT);
    // char HOST_IP[50];
    // while (return_code != 0) {
    //     return_code = socket_host(HOST_ADDRESS, HOST_IP);
    //     if (return_code != 0) {
    //         printf(".");
    //         sleep_ms(100);
    //     }
    // }
    // return_code = -1;
    
    // printf("\nConnecting to Host Target at %s", HOST_IP);
    // SOCKET host_socket = socket_create();
    // while (return_code < 0) {
    //     return_code = socket_connect(host_socket, HOST_IP, HOST_PORT);
    //     if (return_code != 0) {
    //         printf(".");
    //         sleep_ms(100);
    //     }
    // }
    // printf("\nHost Connected!\n");
    
    init_cv();
    init_kinect();
    // kinect_video_ir();
    printf("Kinect Ready\n");
    start_kinect();
    
    // socket_quit();
    return 0;
}