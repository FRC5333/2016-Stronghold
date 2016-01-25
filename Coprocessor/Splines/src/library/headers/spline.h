#ifndef SPLINE_H_DEF
#define SPLINE_H_DEF

#define SPLINE_SAMPLES_FAST (int)1000
#define SPLINE_SAMPLES_LO (int)SPLINE_SAMPLES_FAST*10
#define SPLINE_SAMPLES_HI (int)SPLINE_SAMPLES_LO*10

Coord spline_coords(Spline s, double percentage);
double spline_deriv(Spline s, double percentage);
double spline_deriv_2(double a, double b, double c, double d, double e, double k, double p);
double spline_angle(Spline s, double percentage);

double spline_distance(Spline *s, int sample_count);
double spline_progress_for_distance(Spline s, double distance, int sample_count);

#endif