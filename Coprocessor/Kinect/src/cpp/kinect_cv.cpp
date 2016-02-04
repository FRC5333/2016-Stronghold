#include "kinect_cv.hpp"
#include "kinect.h"

#include <libfreenect.h>

using namespace cv;

Mat videomat, temp_1c, temp_3c;

void *video_thread(void *args) {
    while(1) {
        void *data = video_wait();
        void *depth = depth_fetch();
        int count = kinect_video_bytecount();
    
        prepare_video(data, count, videomat);
    }
}

void prepare_video(void *video, int bytecount, Mat video_mat) {
    if (bytecount == 1) {
        memcpy(temp_1c.data, video, 640*480*bytecount);
        cvtColor(temp_1c, video_mat, CV_GRAY2RGB);
    } else {
        memcpy(video_mat.data, video, 640*480*bytecount);
        cvtColor(video_mat, video_mat, CV_BGR2RGB);
    }
}

void render_text(Mat *mat, std::string str, int x, int y, double scale, int r, int g, int b) {
    putText(*mat, str, Point(x, y), FONT_HERSHEY_COMPLEX, scale, Scalar(r, g, b));
}

void init_cv() {
    temp_1c = Mat(480, 640, CV_8UC1);
    temp_3c = Mat(480, 640, CV_8UC3);
    videomat = Mat(480, 640, CV_8UC3);
    
    // pthread_t listen_thread;
    
    // pthread_create(&listen_thread, NULL, video_thread, NULL);
    Mat mat = imread("out.jpg", CV_LOAD_IMAGE_COLOR);
    char *destination;
    size_t comp_size = compressLZ4()
}