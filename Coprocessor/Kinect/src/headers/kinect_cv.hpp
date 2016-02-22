#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <string>

#ifndef KINECT_CV_HPP
#define KINECT_CV_HPP

cv::Mat video_wait();

void process_kinect(void *video, void *depth);
cv::Mat process_IR(cv::Mat video, void *depth);

void prepare_video(void *video, int bytecount, cv::Mat video_mat);
void render_text(cv::Mat mat, std::string str, int x, int y, double scale, int r, int g, int b);

void init_cv();

#endif
