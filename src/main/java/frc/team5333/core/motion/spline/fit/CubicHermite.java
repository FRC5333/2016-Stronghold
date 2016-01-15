package frc.team5333.core.motion.spline.fit;

import frc.team5333.core.MathUtil;
import frc.team5333.core.motion.spline.Spline;
import frc.team5333.core.motion.spline.SplineFit;
import frc.team5333.core.motion.spline.Waypoint;

public class CubicHermite implements SplineFit {

    @Override
    public Spline fit(double x0, double y0, double angle0, double x1, double y1, double angle1) {
        Spline res = new Spline();
        res.x_offset = x0;
        res.y_offset = y0;

        double delta = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
        res.knot_distance = delta;
        res.angle_offset = Math.atan2(y1 - y0, x1 - x0);

        // Derivs
        double angle0_delta = Math.tan(MathUtil.boundAngleRadians(angle0 - res.angle_offset));
        double angle1_delta = Math.tan(MathUtil.boundAngleRadians(angle1 - res.angle_offset));

        res.a5 = 0; res.b4 = 0;
        res.c3 = (angle0_delta + angle1_delta) / (delta * delta);
        res.d2 = -(2 * angle0_delta + angle1_delta) / delta;
        res.e1 = angle0_delta;

        return res;
    }
}
