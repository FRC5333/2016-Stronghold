#include <stdio.h>
#include <pthread.h>

#include "kinect.h"
#include "kinect_cv.hpp"

freenect_context *f_ctx;
freenect_device *f_dev;
int bytecount = 3;

void *depth_stored;
void *video_stored;

pthread_cond_t video_cv;
pthread_mutex_t video_mtx;
pthread_cond_t depth_cv;
pthread_mutex_t depth_mtx;

int init_kinect() {
    pthread_cond_init(&video_cv, NULL);
    pthread_mutex_init(&video_mtx, NULL);
    
    pthread_cond_init(&depth_cv, NULL);
    pthread_mutex_init(&depth_mtx, NULL);
    
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
    
    freenect_set_tilt_degs(f_dev, 0);
    freenect_set_depth_callback(f_dev, depth_callback);
	freenect_set_video_callback(f_dev, rgb_callback);
    
    freenect_set_depth_mode(f_dev, freenect_find_depth_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_DEPTH_MM));
    // freenect_set_video_mode(f_dev, freenect_find_video_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_VIDEO_RGB));
    freenect_set_video_mode(f_dev, freenect_find_video_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_VIDEO_RGB));
    
    freenect_start_depth(f_dev);
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

// INTERFACING //

void *depth_wait() {
    pthread_cond_wait(&depth_cv, &depth_mtx);
    return depth_stored;
}

void *depth_fetch() {
    return depth_stored;
}

void *video_wait() {
    pthread_cond_wait(&video_cv, &video_mtx);
    return video_stored;
}

void *video_fetch() {
    return video_stored;
}

// CALLBACKS //

void depth_callback(freenect_device *dev, void *depth, uint32_t timestamp) {
    pthread_mutex_lock(&depth_mtx);
    
    depth_stored = depth;
    pthread_cond_broadcast(&depth_cv);
    
    pthread_mutex_unlock(&depth_mtx);
}

void rgb_callback(freenect_device *dev, void *video, uint32_t timestamp) {
    // render_rgb(rgb, timestamp, bytecount);
    pthread_mutex_lock(&video_mtx);
    
    video_stored = video;
    pthread_cond_broadcast(&video_cv);
    
    pthread_mutex_unlock(&video_mtx);
}