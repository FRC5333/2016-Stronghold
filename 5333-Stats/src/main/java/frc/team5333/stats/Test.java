package frc.team5333.stats;

import frc.team5333.core.motion.spline.*;
import frc.team5333.core.motion.spline.fit.CubicHermite;
import frc.team5333.core.motion.spline.fit.QuinticHermite;
import jaci.openrio.toast.lib.math.MathHelper;
import jaci.openrio.toast.lib.math.Vec2D;
import kotlin.Pair;

import java.io.FileWriter;
import java.io.IOException;

public class Test {

    public static void init() {
        Waypoint[] points = new Waypoint[] {
                new Waypoint(-4, -2, 0),
                new Waypoint(-1,  2, MathHelper.d2r(90)),
                new Waypoint( 3,  5, MathHelper.d2r(0)),
                new Waypoint( 5,  2, MathHelper.d2r(-90))
        };

        Trajectory traj = MotionPipeline.createTrajectory(points, new CubicHermite(), 0.01, 15, 10, 60);
        Pair<Trajectory, Trajectory> pair = MotionPipeline.createTrajectoryPair(traj, 2);
        try {
            FileWriter writer = new FileWriter("test_3.csv");

            writer.write("x,original,left,right\n");
            for (int i = 0; i < traj.length(); i++) {
                Trajectory.Section orig = traj.get(i);
                Trajectory.Section left = pair.getFirst().get(i);
                Trajectory.Section right = pair.getSecond().get(i);
                writer.write(String.format("%f,%f,,\n", orig.x, orig.y));
                writer.write(String.format("%f,,%f,\n", left.x, left.y));
                writer.write(String.format("%f,,,%f\n", right.x, right.y));
            }
            writer.close();
        } catch (IOException e) { }
    }

}
