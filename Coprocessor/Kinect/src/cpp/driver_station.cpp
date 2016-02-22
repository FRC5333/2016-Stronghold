#include "kinect_cv.hpp"
#include "driver_station.hpp"
#include "socket.h"
#include "main.h"

#include <pthread.h>

// This file is currently not active, since we decided that getting an image stream of just the ceiling is not
// really necessary.

using namespace cv;
using namespace std;

pthread_t send_thread_id;

vector<int> p;
SOCKET ds_socket;
SocketAddress addr;

void *send_thread(void *args) {
    while(1) {
	Mat video = video_wait();
        char *ds_ip = "10.53.33.194"; 
        // int len = get_ds_ip(ds_ip);
        int len = 1;

        if (len != -1) {
            // Send to Driver Station
            vector<uchar> dest;	
            imencode(".jpg", video, dest, p);
            
            if (inet_aton(ds_ip, &addr.sin_addr) != 0) {
                sendto(ds_socket, &dest[0], dest.size(), 0, (struct sockaddr *) &addr, sizeof(addr)); 
            }
        }
    }
    return NULL;
}  
           
void init_ds() {
    p.push_back(CV_IMWRITE_JPEG_QUALITY);
    p.push_back(75);
    ds_socket = socket_udp_create();
                
    memset((char *) &addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_port = htons(5801);
                
    // pthread_create(&send_thread_id, NULL, send_thread, NULL);
}
           