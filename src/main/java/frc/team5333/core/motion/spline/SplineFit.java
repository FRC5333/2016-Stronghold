package frc.team5333.core.motion.spline;

import jaci.openrio.toast.lib.math.Vec2D;

public interface SplineFit {

    default public Spline fit(Waypoint start, Waypoint end) {
        double x0 = start.positionVector.x();
        double y0 = start.positionVector.y();
        double x1 = end.positionVector.x();
        double y1 = end.positionVector.y();
        double angle0 = start.exitAngle;
        double angle1 = end.exitAngle;
        return fit(x0, y0, angle0, x1, y1, angle1);
    }

    public Spline fit(double x0, double y0, double angle0, double x1, double y1, double angle1);

}
