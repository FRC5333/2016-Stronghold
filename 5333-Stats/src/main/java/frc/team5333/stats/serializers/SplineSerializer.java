package frc.team5333.stats.serializers;

import frc.team5333.core.data.MatchInfo;
import frc.team5333.core.systems.SplineSystem;
import jaci.openrio.toast.core.Toast;
import jaci.openrio.toast.core.io.Storage;
import jaci.openrio.toast.core.thread.Async;

import java.io.File;
import java.io.FileWriter;

public class SplineSerializer {

    public static void serialize(SplineSystem.Trajectory trajectory) {
        double timestamp = Toast.getToast().station().getMatchTime();
        Async.schedule(() -> {
            try {
                File file = Storage.highestPriority("system/stats/splines/" +
                        MatchInfo.matchType.getShortName() + "_" + MatchInfo.matchNum + "_" + timestamp + "s.csv");
                file.getParentFile().mkdirs();

                FileWriter writer = new FileWriter(file);
                writer.write("x,y,position,velocity,acceleration,jerk,heading\n");

                for (SplineSystem.Segment seg : trajectory.getSegments()) {
                    writer.write(
                        String.format("%f,%f,%f,%f,%f,%f,%f\n", seg.getX(), seg.getY(), seg.getPosition(),
                                seg.getVelocity(), seg.getAcceleration(), seg.getJerk(), seg.getHeading())
                    );
                }
                writer.close();
            } catch (Exception e) { }
        });
    }

}
