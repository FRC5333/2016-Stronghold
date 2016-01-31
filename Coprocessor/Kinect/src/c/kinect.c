#include <stdio.h>
#include <libfreenect/libfreenect.h>

#include "kinect.h"

freenect_context *f_ctx;
freenect_device *f_dev;

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
    
    freenect_set_tilt_degs(f_dev, 0);
    
    freenect_set_depth_mode(f_dev, freenect_find_depth_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_DEPTH_MM));
    freenect_set_video_mode(f_dev, freenect_find_video_mode(FREENECT_RESOLUTION_MEDIUM, FREENECT_VIDEO_RGB));
    
    // freenect_start_depth(f_dev);
    // freenect_start_video(f_dev);
    return 0;
}