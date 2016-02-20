#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <string>

#ifndef KINECT_CV_HPP
#define KINECT_CV_HPP

void process_kinect(void *video, void *depth);
void process_IR(Mat video);

void prepare_video(void *video, int bytecount, cv::Mat video_mat);
void render_text(cv::Mat *mat, std::string str, int x, int y, double scale, int r, int g, int b);

void init_cv();

#endif
