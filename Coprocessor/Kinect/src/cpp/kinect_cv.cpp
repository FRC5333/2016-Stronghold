#include "kinect_cv.hpp"
#include "kinect.h"
#include "sleep.h"

#include <pthread.h>
#include <libfreenect.h>

using namespace cv;

Mat videomat, temp_1c, temp_3c, temp_contours;
Scalar hsl_low, hsl_high;
Size blur_size;

pthread_cont_t video_cv;
pthread_mutex_t video_mtx;

Mat video_wait() { 
    pthread_cond_wait(&video_cv, &video_mtx);
    return videomat;
}

void process_kinect(void *video, void *depth) {
    int count = kinect_video_bytecount();
    
    prepare_video(video, count, videomat);
    
    if (count == 1) {
        // IR Stream -> Send Contours to RoboRIO
        process_IR(videomat);
    } else {
        // RGB Stream -> Driver Station Feedback Only
    }
    
    pthread_mutex_lock(&video_mtx);

    pthread_cond_broadcast(&video_cv);      // Broadcast to Threaded Listeners (e.g. Driver Station Sender)
    pthread_mutex_unlock(&video_mtx);
}

void process_IR(Mat video) {
    cvtColor(video, temp_3c, CV_RGB2HLS);
    inRange(temp_3c, hsl_low, hsl_high, temp_3c);
    blur(temp_3c, temp_3c, blur_size);
    
    memcpy(temp_contours.data, temp_3c.data, 640*480*3);
    vector<vector<Point>> contours;
    
    findContours(temp_contours, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_TC89_KCOS);
    drawContours(temp_3c, contours, -1, Scalar(230, 0, 230));
    
    memcpy(video.data, temp_3c.data);
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
    Mat image = imread("out.jpg");
    
    process_IR(image);
    
    imwrite("out2.jpg", image);
    
    temp_1c = Mat(480, 640, CV_8UC1);
    temp_3c = Mat(480, 640, CV_8UC3);
    temp_contours = Mat(480, 640, CV_8UC3);
    videomat = Mat(480, 640, CV_8UC3);
    
    hsl_low = Scalar(0, 16, 0);
    hsl_high = Scalar(255, 255, 255);
    
    blur_size = Size(1, 1);
    
    pthread_cond_init(&video_cv, NULL);
    pthread_mutex_init(&video_mtx, NULL);
}