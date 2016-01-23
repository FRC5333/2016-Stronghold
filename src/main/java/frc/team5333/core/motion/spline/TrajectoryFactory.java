package frc.team5333.core.motion.spline;

public class TrajectoryFactory {

    public static Trajectory create(double dt, double max_vel, double max_acc, double max_jerk, double start_vel,
                                    double start_heading, double target_pos, double target_vel, double target_heading) {
        double checked_max_velocity = Math.min(max_vel,
                    (-(max_acc * max_acc) + Math.sqrt(Math.pow(max_acc, 4) + 4 * (max_jerk * max_jerk * max_acc * target_pos)))
                    / (2 * max_jerk)
                );

        int filter_1_l = (int) Math.ceil((checked_max_velocity / max_acc) / dt);
        int filter_2_l = (int) Math.ceil((max_acc / max_jerk) / dt);

        double impulse = (target_pos / checked_max_velocity) / dt;
        int time = (int) (Math.ceil(filter_1_l + filter_2_l + impulse));

        Trajectory traj = filter(filter_1_l, filter_2_l, dt, 0, checked_max_velocity, impulse, time);

        double delta_heading = target_heading - start_heading;
        for (int i = 0; i < traj.length(); ++i) {
            traj.sections[i].heading = start_heading + delta_heading
                    * (traj.sections[i].position)
                    / traj.sections[traj.length() - 1].position;
        }

        return traj;
    }

    public static Trajectory filter(int filter_1_l, int filter_2_l, double dt, double u, double v, double impulse, int len) {
        if (len == 0) return null;
        Trajectory traj = new Trajectory(len);

        Trajectory.Section last_section = new Trajectory.Section();
        last_section.position = 0;
        last_section.velocity = u;
        last_section.acceleration = 0;
        last_section.jerk = 0;
        last_section.time = dt;

        double[] f1_buffer = new double[len];
        f1_buffer[0] = (u / v) * filter_1_l;
        double f2;

        for (int i = 0; i < len; ++i) {
            double input = Math.min(impulse, 1);
            if (input < 1) {
                input -= 1;
                impulse = 0;
            } else {
                impulse -= input;
            }

            double f1_last;
            if (i > 0)
                f1_last = f1_buffer[i - 1];
            else
                f1_last = f1_buffer[0];

            f1_buffer[i] = Math.max(0.0, Math.min(filter_1_l, f1_last + input));

            f2 = 0;
            for (int j = 0; j < filter_2_l; ++j) {
                if (i - j < 0) break;

                f2 += f1_buffer[i - j];
            }
            f2 = f2 / filter_1_l;

            traj.sections[i].velocity = f2 / filter_2_l * v;

            traj.sections[i].position = (last_section.velocity + traj.sections[i].velocity) / 2.0 * dt + last_section.position;

            traj.sections[i].x = traj.sections[i].position;
            traj.sections[i].y = 0;

            traj.sections[i].acceleration = (traj.sections[i].velocity - last_section.velocity) / dt;
            traj.sections[i].jerk = (traj.sections[i].acceleration - last_section.acceleration) / dt;
            traj.sections[i].time = dt;

            last_section = traj.sections[i];
        }

        return traj;
    }

}
