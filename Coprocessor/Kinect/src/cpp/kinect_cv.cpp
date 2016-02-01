#include "kinect_cv.hpp"
#include "kinect.h"

#include <libfreenect.h>

using namespace cv;

Mat depthimg_1, depthimg_3, rgbimg_1, rgbimg_3, tempimg_1, tempimg_2, tempimg_3;

void *video_thread(void *args) {
    while(1) {
        void *data = video_wait();
        void *depth = depth_fetch();
        int count = kinect_video_bytecount();
    
        // render_rgb(data, count);
    }
}

void render_rgb(void *rgb, int bytecount) {
    if (bytecount == 1) {
        memcpy(rgbimg_1.data, rgb, 640*480*bytecount);
        cvtColor(rgbimg_1, rgbimg_3, CV_GRAY2BGR);
    } else {
        memcpy(rgbimg_3.data, rgb, 640*480*bytecount);
    }
    
    cvtColor(rgbimg_3, tempimg_1, CV_BGR2RGB);

    imwrite("out.jpg", tempimg_2);
}

void render_text(Mat *mat, std::string str, int x, int y, double scale, int r, int g, int b) {
    putText(*mat, str, Point(x, y), FONT_HERSHEY_COMPLEX, scale, Scalar(r, g, b));
}

void init_cv() {
    depthimg_1 = Mat(480, 640, CV_8UC1);
    depthimg_3 = Mat(480, 640, CV_8UC3);
    rgbimg_1 = Mat(480, 640, CV_8UC1);
    rgbimg_3 = Mat(480, 640, CV_8UC3);
    
    pthread_t listen_thread;
    pthread_create(&listen_thread, NULL, video_thread, NULL);
}