package frc.team5333.lib.util;

public class MathUtil {

    public static double boundHalfDeg(double a) {
        while (a >= 180.0) a -= 360.0;
        while (a < -180.0) a += 360.0;
        return a;
    }

}
