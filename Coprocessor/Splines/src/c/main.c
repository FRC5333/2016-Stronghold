#include "splinelib.h"

#include <stdio.h>
#include <time.h>

#include <stdlib.h>

double fRand(double fMin, double fMax) {
    double f = (double)rand() / RAND_MAX;
    return fMin + f * (fMax - fMin);
}

int main() {
    // srand(time(NULL));
    
    Waypoint points[3];
    // int j;
    // for (j = 0; j < 5; j++) {
    //     int x_base = j * 5;
    //     int y_base = j * 3;
    //     Waypoint p = { fRand(x_base, x_base + 3), fRand(y_base, y_base + 3), 0 };
    //     points[j] = p;
    // }
    
    Waypoint p1 = {-4, -1, d2r(45)};
    Waypoint p2 = {-1, 2, 0};
    Waypoint p3 = {2, 4, d2r(0)};
    points[0] = p1;
    points[1] = p2;
    points[2] = p3;
    
    clock_t start = clock(), diff;
    int length;
    Segment *seg = trajectory_generate(&points, 3, FIT_HERMITE_CUBIC, 0.01, 15.0, 10.0, 60.0, &length);
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
    
    free(seg);
    
    return 0;
}