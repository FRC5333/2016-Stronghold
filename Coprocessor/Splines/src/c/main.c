#include "splinelib.h"

#include <stdio.h>
#include <time.h>

#include <stdlib.h>

int main() {
    Waypoint points[3];
    
    Waypoint p1 = {-4, -1, d2r(45)};
    Waypoint p2 = {-1, 2, 0};
    Waypoint p3 = {2, 4, d2r(0)};
    points[0] = p1;
    points[1] = p2;
    points[2] = p3;
    
    clock_t start = clock(), diff;
    TrajectoryCandidate cd;
    trajectory_prepare_candidate(points, 3, FIT_HERMITE_CUBIC, 0.01, 15.0, 10.0, 60.0, &cd);
    Segment seg[cd.length];
    trajectory_generate(&cd, &seg);
    int length = cd.length;
    diff = clock() - start;
    
    int msec = diff * 1000 / CLOCKS_PER_SEC;
    printf("Time taken %d seconds %d milliseconds\n", msec/1000, msec%1000);
    printf("Len: %i\n", length);
    
    FILE *f = fopen("test.csv", "w");
    
    int i;
    fprintf(f, "x,y,v\n");
    for (i = 0; i < 200; i++) {
        Segment s = seg[i];
        fprintf(f, "%f,%f,%f\n", s.x, s.y, s.velocity);
    }
    fclose(f);
    
    return 0;
}