package frc.team5333.core.motion.spline;

import jaci.openrio.toast.lib.math.Vec2D;

public class Waypoint {

    public Vec2D positionVector;
    public double exitAngle;

    public Waypoint(Vec2D positionVector, double exitAngle) {
        this.positionVector = positionVector;
        this.exitAngle = exitAngle;
    }

}
