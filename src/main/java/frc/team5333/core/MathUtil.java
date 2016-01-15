package frc.team5333.core;

public class MathUtil {

    public static double boundAngleRadians(double angle) {
        double newAngle = angle % (2*Math.PI);
        if (newAngle < 0) newAngle = (2*Math.PI) + newAngle;
        return newAngle;
    }

}
