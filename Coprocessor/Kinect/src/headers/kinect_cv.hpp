#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc.hpp>
#include <string>

#ifndef KINECT_CV_HPP
#define KINECT_CV_HPP

void render_rgb(void *rgb, int bytecount);
void render_text(cv::Mat *mat, std::string str, int x, int y, double scale, int r, int g, int b);

void init_cv();

#endif