#ifndef TRAJECTORY_H_DEF
#define TRAJECTORY_H_DEF

void trajectory_copy(Segment *src, Segment *dest, int length);

TrajectoryInfo trajectory_prepare(TrajectoryConfig c);
void trajectory_create(TrajectoryInfo info, TrajectoryConfig c, Segment *seg);
void trajectory_fromSecondOrderFilter(int filter_1_l, int filter_2_l, 
        double dt, double u, double v, double impulse, int len, Segment *t);

#endif