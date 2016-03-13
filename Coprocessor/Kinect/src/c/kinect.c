#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>  

#include "kinect.h"
#include "kinect_cv.hpp"

#define THREAD_COUNT 3

freenect_context *f_ctx;
freenect_device *f_dev;
int bytecount = 1;

void *depth_stored;
void *video_buf;

pthread_cond_t video_cv;
pthread_mutex_t video_mtx;

pthread_t tids[THREAD_COUNT];

void *thread_func(void *args) {
    printf("Thread Launched!");
    while (1) {
        pthread_cond_wait(&video_cv, &video_mtx);
        void *video = malloc(640*480);
        memcpy(video.data, video_buf, 640*480);
        process_kinect(video);
        free(video);
    }
}

int init_kinect() {
    if (freenect_init(&f_ctx, NULL) < 0) {
		printf("Freenect Framework Initialization Failed!\n");
		return 1;
	}
    
    freenect_select_subdevices(f_ctx, (freenect_device_flags)(FREENECT_DEVICE_MOTOR | FREENECT_DEVICE_CAMERA));
    int nr_devices = freenect_num_devices (f_ctx);
    
    if (nr_devices < 1) {
        printf("No Kinect Sensors were detected! %i\n", nr_devices);
		freenect_shutdown(f_ctx);
		return 1;
	}
    
    if (freenect_open_device(f_ctx, &f_dev, 0) < 0) {
		printf("Could not open Kinect Device\n");
		freenect_shutdown(f_ctx);
		return 1;
	}
    
    pthread_cond_init(&video_cv, NULL);
    pthread_mutex_init(&video_mtx, NULL);
    
    int i;
    for (i = 0; i < THREAD_COUNT; i++) {
        pthread_create(&tid[i], NULL, thread_func, NULL);
        pthread_setschedprio(tid[i], 8);
    }
    
    pthread_setschedprio(pthread_self(), 7);
    
    freenect_set_tilt_degs(f_dev, 0);
    freenect_set_depth_callback(f_dev, depth_callback);
	freenect_set_video_callback(f_dev, rgb_callback);
    
    freenect_set_depth_mode(f_dev, freenect_find_depth_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_DEPTH_MM));
    // freenect_set_video_mode(f_dev, freenect_find_video_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_VIDEO_RGB));
    freenect_set_video_mode(f_dev, freenect_find_video_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_VIDEO_IR_8BIT));
    
    // freenect_start_depth(f_dev);     // We're not using Depth Streams right now.
    freenect_start_video(f_dev);
    return 0;
}

void start_kinect() {
    while(freenect_process_events(f_ctx) >= 0);
}

void kinect_video_rgb() {
    bytecount = 3;
    freenect_stop_video(f_dev);
    freenect_set_video_mode(f_dev, freenect_find_video_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_VIDEO_RGB));
    freenect_start_video(f_dev);
}

void kinect_video_ir() {
    bytecount = 1;
    freenect_stop_video(f_dev);
    freenect_set_video_mode(f_dev, freenect_find_video_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_VIDEO_IR_8BIT));
    freenect_start_video(f_dev);
}

int kinect_video_bytecount() {
    return bytecount;
}

// CALLBACKS //

void depth_callback(freenect_device *dev, void *depth, uint32_t timestamp) {
    depth_stored = depth;
}

void rgb_callback(freenect_device *dev, void *video, uint32_t timestamp) {
    void *tempbuf = video;
    
    pthread_mutex_lock(&video_mtx);
    video_buf = tempbuf;
    pthread_cond_signal(&video_cv);         // Signal a single thread. This means processing waterfalls across all threads.
    pthread_mutex_unlock(&video_mtx);
    // process_kinect(video);
}