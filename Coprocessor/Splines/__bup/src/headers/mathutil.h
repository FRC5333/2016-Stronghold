#include <math.h>

#ifndef MATH_UTIL_H_DEF
#define MATH_UTIL_H_DEF

#define PI 3.14159265358979323846
#define TAU PI*2

#define MIN(a,b) (((a)<(b))?(a):(b))
#define MAX(a,b) (((a)>(b))?(a):(b))

double bound_radians(double angle);

double r2d(double angleInRads);

double d2r(double angleInDegrees);

#endif