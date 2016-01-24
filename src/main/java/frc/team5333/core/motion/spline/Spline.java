package frc.team5333.core.motion.spline;

import frc.team5333.core.motion.spline.MathUtil;
import jaci.openrio.toast.lib.math.Vec2D;

public class Spline {

    // Follows spline formula (ax^5 + bx^4 + cx^3 + dx^2 + ex). Coefficient groups
    public double a5;
    public double b4;
    public double c3;
    public double d2;
    public double e1;

    // Real-world offsets
    public double x_offset;
    public double y_offset;
    public double angle_offset;
    public double knot_distance;
    public double arc_length;

    public Vec2D getXandY(double percentage) {
        double[] result = new double[2];

        percentage = Math.max(Math.min(percentage, 1), 0);
        double x_val = percentage * knot_distance;
        double y_val = (a5 * x_val + b4) * x_val * x_val * x_val * x_val
                + c3 * x_val * x_val * x_val + d2 * x_val * x_val + e1 * x_val;

        double cos_theta = Math.cos(angle_offset);
        double sin_theta = Math.sin(angle_offset);

        result[0] = x_val * cos_theta - y_val * sin_theta + x_offset;
        result[1] = x_val * sin_theta + y_val * cos_theta + y_offset;
        return new Vec2D(result[0], result[1]);
    }

    public void serialize(double step, SplineConsumer consumer) {
        for (double i = 0; i < 1; i+=step) {
            Vec2D v = getXandY(i);
            consumer.write(v.x(), v.y(), i);
        }
    }

    public double length() {
        if (arc_length > 0) {
            return arc_length;
        }

        final int kNumSamples = 100000;
        double arc_length = 0;
        double t, dydt;
        double integrand, last_integrand
                = Math.sqrt(1 + deriv(0) * deriv(0)) / kNumSamples;
        for (int i = 1; i <= kNumSamples; ++i) {
            t = ((double) i) / kNumSamples;
            dydt = deriv(t);
            integrand = Math.sqrt(1 + dydt * dydt) / kNumSamples;
            arc_length += (integrand + last_integrand) / 2;
            last_integrand = integrand;
        }
        this.arc_length = knot_distance * arc_length;
        return this.arc_length;
    }

    public double deriv(double percentage) {
        percentage = Math.max(Math.min(percentage, 1), 0);

        double x_hat = percentage * knot_distance;
        double yp_hat = (5 * a5 * x_hat + 4 * b4) * x_hat * x_hat * x_hat + 3 * c3 * x_hat * x_hat
                + 2 * d2 * x_hat + e1;

        return yp_hat;
    }

    public double angleAt(double percentage) {
        return MathUtil.boundAngleRadians(Math.atan(deriv(percentage)) + angle_offset);
    }

    public double progressForDist(double distance) {
        final int RESOLUTION = 100000;
        double arc_length = 0;
        double t = 0;
        double last_arc_length = 0;
        double dydt;
        double integrand = 0, last_integrand = Math.sqrt(1 + deriv(0) * deriv(0)) / RESOLUTION;
        distance /= knot_distance;
        for (int i = 1; i <= RESOLUTION; ++i) {
            t = ((double) i) / RESOLUTION;
            dydt = deriv(t);
            integrand = Math.sqrt(1 + dydt * dydt) / RESOLUTION;
            arc_length += (integrand + last_integrand) / 2;
            if (arc_length > distance) {
                break;
            }
            last_integrand = integrand;
            last_arc_length = arc_length;
        }

        double interpolated = t;
        if (arc_length != last_arc_length) {
            interpolated += ((distance - last_arc_length)
                    / (arc_length - last_arc_length) - 1) / (double) RESOLUTION;
        }
        return interpolated;
    }

    public String toString() {
        return String.format("Spline[%.2fx^5 + %.2fx^4 + %.2fx^3 + %.2fx^2 + %.2fx, x=%.2f, y=%.2f, theta=%.2f, knot=%.2f, arc=%.2f]",
                a5, b4, c3, d2, e1, x_offset, y_offset, angle_offset, knot_distance, arc_length);
    }

    public static interface SplineConsumer {
        public void write(double x, double y, double progress);
    }

}
