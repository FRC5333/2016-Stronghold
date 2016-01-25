#ifndef MOTION_H_DEF
#define MOTION_H_DEF

int trajectory_prepare_candidate(Waypoint *path, int path_length, void (*fit)(Waypoint,Waypoint,Spline*), int sample_count, double dt,
        double max_velocity, double max_acceleration, double max_jerk, TrajectoryCandidate *cand);
int trajectory_generate(TrajectoryCandidate *c, Segment *segments);

#endif