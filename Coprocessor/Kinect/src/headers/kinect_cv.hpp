#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <string>

#ifndef KINECT_CV_HPP
#define KINECT_CV_HPP

void *kinect_thread_func(pthread_cond_t *cv_v, pthread_mutex_t *mtx_v, void *video, int tid);

void process_kinect(int tid);
std::vector<cv::Rect> process_IR(int tid);

void prepare_video(void *video, int bytecount, int tid);

void init_cv();

#endif
