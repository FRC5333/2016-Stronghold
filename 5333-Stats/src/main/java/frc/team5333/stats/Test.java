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
        SplineFit fit = new CubicHermite();
        Waypoint start = new Waypoint(new Vec2D(-10, 5), 0);
        Waypoint quarter = new Waypoint(new Vec2D(-5, -1), MathHelper.d2r(-80));
        Waypoint half = new Waypoint(new Vec2D(0, -4), 0);
        Waypoint threequarter = new Waypoint(new Vec2D(5, 2), MathHelper.d2r(50));
        Waypoint end = new Waypoint(new Vec2D(10, 4), MathHelper.d2r(-20));

        Spline[] s = new Spline[] {
            fit.fit(start, quarter), fit.fit(quarter, half),
            fit.fit(half, threequarter), fit.fit(threequarter, end)
        };

        try {
            FileWriter writer = new FileWriter("test.csv");

            writer.write("x,s1,s2,s3,s4\n");
            for (double i = 0; i < 1; i+=0.01) {
                for (int si = 0; si < s.length; si++) {
                    Spline spline = s[si];
                    Vec2D d = spline.getXandY(i);
                    if (si == 0) writer.write(d.x() + "," + d.y() + ",,,\n");
                    if (si == 1) writer.write(d.x() + ",," + d.y() + ",,\n");
                    if (si == 2) writer.write(d.x() + ",,," + d.y() + ",\n");
                    if (si == 3) writer.write(d.x() + ",,,," + d.y() + "\n");
                }
            }
            writer.close();
        } catch (IOException e) { }
    }

}
