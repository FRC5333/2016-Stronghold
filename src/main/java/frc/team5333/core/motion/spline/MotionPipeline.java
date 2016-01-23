package frc.team5333.core.motion.spline;

import jaci.openrio.toast.lib.math.Vec2D;
import kotlin.Pair;

public class MotionPipeline {

    public static Trajectory createTrajectory(Waypoint[] path, SplineFit fitter, double time,
                                              double max_velocity, double max_acceleration, double max_jerk) {
        if (path.length < 2)            // Pretty useless tbh
            return null;

        Spline[] splines = new Spline[path.length - 1];
        double[] spline_lengths = new double[splines.length];
        double total_length = 0;

        for (int i = 0; i < splines.length; ++i) {
            splines[i] = fitter.fit(path[i], path[i+1]);
            spline_lengths[i] = splines[i].length();
            total_length += spline_lengths[i];
        }

        Trajectory traj = TrajectoryFactory.create(time, max_velocity, max_acceleration, max_jerk, 0, path[0].exitAngle,
                total_length, 0, path[0].exitAngle);

        int spline_i = 0;
        double spline_pos_initial = 0;
        double splines_complete = 0;
        for (int i = 0; i < traj.length(); ++i) {
            double pos = traj.get(i).position;

            boolean found = false;
            while (!found) {
                double pos_relative = pos - spline_pos_initial;
                if (pos_relative <= spline_lengths[spline_i]) {
                    double percentage = splines[spline_i].progressForDist(pos_relative);
                    traj.get(i).heading = splines[spline_i].angleAt(percentage);
                    Vec2D coords = splines[spline_i].getXandY(percentage);
                    traj.get(i).x = coords.x();
                    traj.get(i).y = coords.y();
                    found = true;
                } else if (spline_i < splines.length - 1) {
                    splines_complete += spline_lengths[spline_i];
                    spline_pos_initial = splines_complete;
                    ++spline_i;
                } else {
                    traj.get(i).heading = splines[splines.length - 1].angleAt(1.0);
                    Vec2D coords = splines[splines.length - 1].getXandY(1.0);
                    traj.get(i).x = coords.x();
                    traj.get(i).y = coords.y();
                    found = true;
                }
            }
        }

        return traj;
    }

    public static Pair<Trajectory, Trajectory> createTrajectoryPair(Trajectory original, double width) {
        Pair<Trajectory, Trajectory> out = new Pair<>(original.copy(), original.copy());
        Trajectory first = out.getFirst();
        Trajectory second = out.getSecond();

        double w2 = width / 2;

        for (int i = 0; i < original.length(); i++) {
            Trajectory.Section section = original.get(i);
            double ca = Math.cos(section.heading);
            double sa = Math.sin(section.heading);

            Trajectory.Section section1 = first.get(i);
            section1.x = section.x - (w2 * sa);
            section1.y = section.y + (w2 * ca);

            if (i > 0) {
                Trajectory.Section last = first.get(i - 1);
                double distance = Math.sqrt(
                        (section1.x - last.x) * (section1.x - last.x)
                        +
                        (section1.y - last.y) * (section1.y - last.y)
                );
                section1.position = last.position + distance;
                section1.velocity = distance / section1.time;
                section1.acceleration = (section1.velocity - last.velocity) / section1.time;
                section1.jerk = (section1.acceleration - last.acceleration) / section1.time;
            }

            Trajectory.Section section2 = second.get(i);
            section2.x = section.x + (w2 * sa);
            section2.y = section.y - (w2 * ca);

            if (i > 0) {
                Trajectory.Section last = second.get(i - 1);
                double distance = Math.sqrt(
                        (section2.x - last.x) * (section2.x - last.x)
                        +
                        (section2.y - last.y) * (section2.y - last.y)
                );
                section2.position = last.position + distance;
                section2.velocity = distance / section2.time;
                section2.acceleration = (section2.velocity - last.velocity) / section2.time;
                section2.jerk = (section2.acceleration - last.acceleration) / section2.time;
            }
        }
        return out;
    }

}
