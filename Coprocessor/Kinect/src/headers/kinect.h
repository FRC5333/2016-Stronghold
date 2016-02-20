#include <libfreenect.h>

#ifndef KINECT_H_DEF
#define KINECT_H_DEF

int init_kinect();
void start_kinect();

void kinect_video_rgb();
void kinect_video_ir();
int kinect_video_bytecount();

void depth_callback(freenect_device *dev, void *depth, uint32_t timestamp);
void rgb_callback(freenect_device *dev, void *rgb, uint32_t timestamp);

#endif