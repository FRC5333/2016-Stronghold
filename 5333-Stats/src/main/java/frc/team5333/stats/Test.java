package frc.team5333.stats;

import frc.team5333.core.motion.spline.Spline;
import frc.team5333.core.motion.spline.SplineFit;
import frc.team5333.core.motion.spline.Waypoint;
import frc.team5333.core.motion.spline.fit.CubicHermite;
import jaci.openrio.toast.lib.math.MathHelper;
import jaci.openrio.toast.lib.math.Vec2D;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

    public static void init() {
        SplineFit hermite = new CubicHermite();
        Waypoint start = new Waypoint(new Vec2D(-4, 2), 0);
        Waypoint middle = new Waypoint(new Vec2D(0, 5), MathHelper.d2r(20));
        Waypoint end = new Waypoint(new Vec2D(4, 1), MathHelper.d2r(-45));

        Spline fit1 = hermite.fit(start, middle);
        Spline fit2 = hermite.fit(middle, end);

        try {
            FileWriter writer = new FileWriter("test.csv");
            writer.write("x,y1,y2\n");
            for (double i = 0; i < 1; i+=0.01) {
                double[] val_1 = fit1.getXandY(i);
                double[] val_2 = fit2.getXandY(i);
                writer.write(val_1[0] + "," + val_1[1] + ",\n");
                writer.write(val_2[0] + ",," + val_2[1] + "\n");
            }
            writer.close();
        } catch (IOException e) {
        }
    }

}
