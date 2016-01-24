#ifndef MOTION_H_DEF
#define MOTION_H_DEF

int trajectory_generate(Waypoint *path, int path_length, Spline (*fit)(Waypoint,Waypoint), double dt,
        double max_velocity, double max_acceleration, double max_jerk, Segment *returnSegments);

#endif