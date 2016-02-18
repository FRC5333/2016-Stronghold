package frc.team5333.stats.serializers;

import frc.team5333.core.control.strategy.StrategyMotionProfile;
import frc.team5333.core.data.MatchInfo;
import frc.team5333.core.events.StrategyEvent;
import frc.team5333.core.control.profiling.SplineSystem;
import frc.team5333.lib.events.EventBus;
import frc.team5333.lib.events.EventListener;
import jaci.openrio.toast.core.Toast;
import jaci.openrio.toast.core.io.Storage;
import jaci.openrio.toast.core.thread.Async;

import java.io.File;
import java.io.FileWriter;

public class SplineSerializer {

    public static void init() {
        EventBus.INSTANCE.register(SplineSerializer.class);
    }

    @EventListener
    public static void stratChanged(StrategyEvent.StrategyChangeEvent event) {
        if (event.getNewStrategy() instanceof StrategyMotionProfile) {
            StrategyMotionProfile s = (StrategyMotionProfile) event.getNewStrategy();
            serialize(s.getTrajectory().getFirst(), s.getTrajectory().getSecond());
        }
    }

    public static void serialize(SplineSystem.Trajectory trajectory, SplineSystem.Trajectory trajectory2) {
        double timestamp = Toast.getToast().station().getMatchTime();
        Async.schedule(() -> {
            try {
                File file = Storage.highestPriority("system/stats/splines/" +
                        MatchInfo.matchType.getShortName() + "_" + MatchInfo.matchNum + "_" + timestamp + "s.csv");
                file.getParentFile().mkdirs();

                FileWriter writer = new FileWriter(file);
                writer.write("x1,y1,x2,y2,position1,position2,velocity1,velocity2,acceleration1,acceleration2\n");

                for (int i = 0; i < trajectory.getLength(); i++) {
                    SplineSystem.Segment seg1 = trajectory.get(i);
                    SplineSystem.Segment seg2 = trajectory2.get(i);
                    writer.write(
                            String.format("%f,%f,%f,%f,%f,%f,%f,%f,%f,%f\n", seg1.getX(), seg1.getY(), seg2.getX(), seg2.getY(),
                                    seg1.getPosition(), seg2.getPosition(), seg1.getVelocity(), seg2.getVelocity(),
                                    seg1.getAcceleration(), seg2.getAcceleration())
                    );
                }
                writer.close();
            } catch (Exception e) { }
        });
    }

}
