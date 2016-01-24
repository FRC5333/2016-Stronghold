#ifndef SPLINE_H_DEF
#define SPLINE_H_DEF

#define SPLINE_NUM_SAMPLES_I (int)100000
#define SPLINE_NUM_SAMPLES_D (double)SPLINE_NUM_SAMPLES_I

Coord spline_coords(Spline s, double percentage);
double spline_deriv(Spline s, double percentage);
double spline_deriv_2(double a, double b, double c, double d, double e, double k, double p);
double spline_angle(Spline s, double percentage);

double spline_distance(Spline *s);
double spline_progress_for_distance(Spline s, double distance);

#endif