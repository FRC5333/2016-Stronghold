#include "structs.h"

#ifndef FIT_H_DEF
#define FIT_H_DEF

void fit_hermite_pre(Waypoint a, Waypoint b, Spline *s);
void fit_hermite_cubic(Waypoint a, Waypoint b, Spline *s);
void fit_hermite_quintic(Waypoint a, Waypoint b, Spline *s);

#define FIT_HERMITE_CUBIC   &fit_hermite_cubic
#define FIT_HERMITE_QUINTIC &fit_hermite_quintic

#endif