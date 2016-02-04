#include "kinect_cv.hpp"

#ifndef COMPRESSION_HPP_DEF
#define COMPRESSION_HPP_DEF

size_t compressLZ4(cv::Mat mat, char *destarray);
cv::Mat decompressLZ4(char *buf, size_t size);

#endif