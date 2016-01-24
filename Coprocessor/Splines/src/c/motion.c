#include "splinelib.h"

#include <stdlib.h>
#include <pthread.h>

typedef struct {
    Spline *splinesptr;
    double *length;
    double *lengthsptr;
    int *id;
} targs;

void *threadDistance(void *arg) {
    targs t = *((targs *) arg);
    
    int id = *t.id;
    Spline *s = &t.splinesptr[id];
    double dist = spline_distance(s);
    
    *t.length += dist;
    *t.lengthsptr = dist;
    
    free(t.id);
    free(arg);
    return NULL;
}

// Usage: 
// int length;
// Segment *segs = trajectory_generate(&waypoints_array, waypoints_size, fit_function_ptr, time, vel, acceleration, jerk, &length);
// Don't forget to free the 'segs' object when you are done with it, or else the program will memory leak.
Segment *trajectory_generate(Waypoint *path, int path_length, void (*fit)(Waypoint,Waypoint,Spline*), double dt,
        double max_velocity, double max_acceleration, double max_jerk, int *length) {
    if (path_length < 2) return 0;
    
    Spline splines[path_length - 1];
    double splineLengths[path_length - 1];
    double totalLength = 0;
    
    pthread_t pth[path_length-1];
    
    int i;
    for (i = 0; i < path_length-1; i++) {
        // TODO: Move threaded stuff over to the post-processing of progress_for_distance
        Spline s;
        fit(path[i], path[i+1], &s);
        splines[i] = s;
        
        int *ida = malloc(sizeof(int));
        *ida = i;
        
        targs args1 = { splines, &totalLength, &splineLengths[i], ida };
        targs *args = malloc(sizeof(targs));
        
        *args = args1;
        pthread_create(&pth[i], NULL, threadDistance, args);
    }
    
    for (i = 0; i < path_length-1; i++) {
        pthread_join(pth[i], NULL);
    }
    
    TrajectoryConfig config = {dt, max_velocity, max_acceleration, max_jerk, 0, path[0].angle,
        totalLength, 0, path[0].angle};
    TrajectoryInfo info = trajectory_prepare(config);
    int trajectory_length = info.length;
    
    Segment *segments = malloc(trajectory_length * sizeof(Segment));
    trajectory_create(info, config, segments);
    
    int spline_i = 0;
    double spline_pos_initial, splines_complete;
    
    for (i = 0; i < trajectory_length; ++i) {
        double pos = segments[i].position;

        int found = 0;
        while (!found) {
            double pos_relative = pos - spline_pos_initial;
            if (pos_relative <= splineLengths[spline_i]) {
                Spline si = splines[spline_i];
                double percentage = spline_progress_for_distance(si, pos_relative);
                Coord coords = spline_coords(si, percentage);
                segments[i].heading = spline_angle(si, percentage);
                segments[i].x = coords.x;
                segments[i].y = coords.y;
                found = 1;
            } else if (spline_i < path_length - 2) {
                splines_complete += splineLengths[spline_i];
                spline_pos_initial = splines_complete;
                spline_i += 1;
            } else {
                Spline si = splines[path_length - 2];
                segments[i].heading = spline_angle(si, 1.0);
                Coord coords = spline_coords(si, 1.0);
                segments[i].x = coords.x;
                segments[i].y = coords.y;
                found = 1;
            }
        }
    }
    
    *length = trajectory_length;
    return segments;
}