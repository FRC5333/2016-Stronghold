package frc.team5333.core.motion.spline;

public class MotionPipeline {

    public static Trajectory createTrajectory(Waypoint[] path, SplineFit fitter, double time,
                                              double max_velocity, double max_acceleration, double max_jerk) {
        if (path.length < 2)            // Pretty useless tbh
            return null;

        Spline[] splines = new Spline[path.length - 1];
        double[] spline_lengths = new double[splines.length];
        double total_length;

        for (int i = 0; i < splines.length; ++i) {
            splines[i] = fitter.fit(path[i], path[i+1]);
            spline_lengths[i] = splines[i].length();
            total_length += spline_lengths[i];
        }
    }

}
