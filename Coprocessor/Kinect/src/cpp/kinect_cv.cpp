#include "kinect_cv.hpp"
#include "kinect.h"
#include "conversions.h"
#include "main.h"

#include <pthread.h>
#include <libfreenect.h>

using namespace std;
using namespace cv;

Mat videomat, temp_1c, temp_3c;
Scalar hsl_low, hsl_high;
Size blur_size;

vector<Rect> ir_rects;
vector<uint16_t> ir_depths;
pthread_cond_t video_cv;
pthread_mutex_t video_mtx;

Mat video_wait() { 
    pthread_cond_wait(&video_cv, &video_mtx);
    return videomat;
}

void process_kinect(void *video, void *depth) {
    int count = kinect_video_bytecount();
    char buf[4];
    
    prepare_video(video, count, videomat);
    	
    if (count == 1) {
        // IR Stream -> Send Contour Bounds to RoboRIO
        videomat = process_IR(videomat, depth);
        intToBytes(0xBA, buf);
        send_to_rio(buf, 4);
        int i;
        for (i = 0; i < ir_rects.size(); i++) {
            Rect r = ir_rects[i];
            int depth_mm = ir_depths[i];

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
            
            intToBytes(depth_mm, buf);
            send_to_rio(buf, 4);
            
            int depth_local = (int)(depth_mm * cos(0.523599));
            intToBytes(depth_local, buf);
            send_to_rio(buf, 4);
        }
        
        intToBytes(0xBC, buf);
        send_to_rio(buf, 4);
    } else {
        // RGB Stream -> Driver Station Feedback Only
        intToBytes(0xCA, buf);
        send_to_rio(buf, 4);
    }
    
    printf("KinectFrame\n");
    
    pthread_mutex_lock(&video_mtx);

    pthread_cond_broadcast(&video_cv);      // Broadcast to Threaded Listeners (e.g. Driver Station Sender)
    pthread_mutex_unlock(&video_mtx);
}

Mat process_IR(Mat video, void *depth) {
    ir_rects.clear();
    
    Mat depth_mat = Mat(480, 640, CV_16UC1);
    memcpy(depth_mat.data, depth, 640*480*2);
    
    flip(depth_mat, depth_mat, 0);
    flip(video, video, 0);
    
    Mat original = video.clone();
    Mat tmp;
    cvtColor(video, tmp, CV_RGB2HLS);
    inRange(tmp, hsl_low, hsl_high, tmp);
    blur(tmp, tmp, blur_size);
    
    Mat temp_contours = tmp.clone();
    
    vector<vector<Point> > contours;
    vector<vector<Point> > filteredContours;
    
    findContours(temp_contours, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_TC89_KCOS);

    int i;
    for (i = 0; i < contours.size(); i++) {
        vector<Point> contour = contours[i];
        double area = contourArea(contour);
        vector<Point> hull;
        convexHull(contour, hull);
        double solidity = 100 * area / contourArea(hull);
        
        if (area > 300.0 && solidity < 75.0) {
            filteredContours.push_back(contour);
            Rect r = boundingRect(contour);
            
            uint16_t d = depth_mat.at<uint16_t>(r.x-5, r.y-5);
            ir_depths.push_back(d);
            ir_rects.push_back(r);
        }
    }   
        
    // for (i = 0; i < filteredContours.size(); i++) {
    //     drawContours(video, filteredContours, i, Scalar(255, 0, 255), 2);
    //     Rect r = ir_rects[i];
    //     rectangle(video, r.tl(), r.br(), Scalar(0, 0, 255), 1);
    //     render_text(video, format("[%d,%d]", r.x, r.y), r.x - r.width / 2, r.y - r.height / 2, 0.5, 255, 0, 255);
    //     render_text(video, format("[<< %d]", ir_depths[i]), r.x - 5, r.y - 5, 0.5, 0, 255, 0);
    // }
    
    return video.clone();
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

void render_text(Mat mat, std::string str, int x, int y, double scale, int r, int g, int b) {
    putText(mat, str, Point(x, y), FONT_HERSHEY_COMPLEX, scale, Scalar(r, g, b));
}

void init_cv() {
    temp_1c = Mat(480, 640, CV_8UC1);
    temp_3c = Mat(480, 640, CV_8UC3);
    videomat = Mat(480, 640, CV_8UC3);
    
    hsl_low = Scalar(0, 16, 0);
    hsl_high = Scalar(255, 255, 255);
    
    blur_size = Size(1, 1);
    
    pthread_cond_init(&video_cv, NULL);
    pthread_mutex_init(&video_mtx, NULL);
}