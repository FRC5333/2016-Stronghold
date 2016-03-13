#include "kinect_cv.hpp"
#include "kinect.h"
#include "conversions.h"
#include "main.h"

#include <pthread.h>
#include <libfreenect.h>

using namespace std;
using namespace cv;

Mat videomat[THREAD_COUNT], temp_1c[THREAD_COUNT];
Scalar hsl_low, hsl_high;
Size blur_size;

void *kinect_thread_func(pthread_cond_t *cv_v, pthread_mutex_t *mtx_v, void *video, int tid) {
    pthread_mutex_lock(mtx_v);
    pthread_cond_wait(cv_v, mtx_v);
    prepare_video(video, kinect_video_bytecount(), tid);
    pthread_mutex_unlock(mtx_v);
    
    process_kinect(tid);
    return NULL;
}

void process_kinect(int tid) {
    int count = kinect_video_bytecount();
    char buf[4];
    	
    if (count == 1) {
        // IR Stream -> Send Contour Bounds to RoboRIO
        vector<Rect> ir_rects = process_IR(tid);
        intToBytes(0xBA, buf);
        send_to_rio(buf, 4);
        int i;
        for (i = 0; i < ir_rects.size(); i++) {
            Rect r = ir_rects[i];

            intToBytes(0xBB, buf);
            send_to_rio(buf, 4);
            
            intToBytes(r.x, buf);
            send_to_rio(buf, 4);
            
            intToBytes(r.y, buf);
            send_to_rio(buf, 4);
            
            intToBytes(r.width, buf);
            send_to_rio(buf, 4);
            
            intToBytes(r.height, buf);
            send_to_rio(buf, 4);
        }
        
        intToBytes(0xBC, buf);
        send_to_rio(buf, 4);
    } else {
        // RGB Stream -> Driver Station Feedback Only
        intToBytes(0xCA, buf);
        send_to_rio(buf, 4);
    }
}

vector<Rect> process_IR(int tid) {
    vector<Rect> ir_rects;
    
    Mat video = videomat[tid];
    
    flip(video, video, 0);
    
    Mat tmp;
    cvtColor(video, tmp, CV_RGB2HLS);
    inRange(tmp, hsl_low, hsl_high, tmp);
    
    vector<vector<Point> > contours;
    
    findContours(tmp, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_TC89_KCOS);

    int i;
    for (i = 0; i < contours.size(); i++) {
        vector<Point> contour = contours[i];
        Rect r = boundingRect(contour);
       
        double area = contourArea(contour);
        vector<Point> hull;
        convexHull(contour, hull);
        double solidity = 100 * area / contourArea(hull);
        
        if (area > 300.0 && solidity < 75.0) {
            ir_rects.push_back(r);
        }
    }   
    
    return ir_rects;
}

void prepare_video(void *video, int bytecount, int tid) {
    if (bytecount == 1) {
        memcpy(temp_1c[tid].data, video, 640*480*bytecount);
        cvtColor(temp_1c[tid], videomat[tid], CV_GRAY2RGB);
    } else {
        memcpy(videomat[tid].data, video, 640*480*bytecount);
        cvtColor(videomat[tid], videomat[tid], CV_BGR2RGB);
    }
}

void init_cv() {
    int i;
    for (i = 0; i < THREAD_COUNT; i++) {
        videomat[i] = Mat(480, 640, CV_8UC3);
        temp_1c[i] = Mat(480, 640, CV_8UC1);
    }
    
    hsl_low = Scalar(0, 16, 0);
    hsl_high = Scalar(255, 255, 255);
    
    blur_size = Size(1, 1);
}