#include "structs.h"

#ifndef FIT_H_DEF
#define FIT_H_DEF

Spline fit_hermite_pre(Waypoint a, Waypoint b);
Spline fit_hermite_cubic(Waypoint a, Waypoint b);
Spline fit_hermite_quintic(Waypoint a, Waypoint b);

#define FIT_HERMITE_CUBIC   &fit_hermite_cubic
#define FIT_HERMITE_QUINTIC &fit_hermite_quintic

#endif