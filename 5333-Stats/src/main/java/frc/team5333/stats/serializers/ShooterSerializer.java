package frc.team5333.stats.serializers;

import frc.team5333.core.data.MatchInfo;
import frc.team5333.core.events.ShootEvent;
import frc.team5333.core.systems.ShooterSystem;
import frc.team5333.lib.events.EventBus;
import frc.team5333.lib.events.EventListener;
import frc.team5333.lib.events.EventPriority;
import jaci.openrio.toast.core.Toast;
import jaci.openrio.toast.core.io.Storage;
import jaci.openrio.toast.core.thread.Async;
import kotlin.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShooterSerializer {

    public static void init() {
        EventBus.INSTANCE.register(ShooterSerializer.class);
    }

    @EventListener(priority = EventPriority.LOWEST)
    public static void trigger(ShootEvent event) {
        double timestamp = Toast.getToast().station().getMatchTime();
        ShooterSystem.ShooterTrajectory traj = event.getTrajectory();
        Async.schedule(() -> {
            try {
                File file = Storage.highestPriority("system/stats/shooter/" +
                        MatchInfo.matchType.getShortName() + "_" + MatchInfo.matchNum + "_" + timestamp + "s.csv");
                file.getParentFile().mkdirs();

                FileWriter writer = new FileWriter(file);
                writer.write("x,y,xv,yv\n");

//                traj.it(0.01).forEach((x) -> {
//                    double time = ShooterSystem.INSTANCE.calculate_time_at_x(traj.getVel(), traj.getLaunch_angle(), x);
//                    Pair<Double, Double> velocities = ShooterSystem.INSTANCE.calculate_velocities_at_time(traj.getVel(), traj.getLaunch_angle(), time);
//                    double y = traj.y_at(x);
//                    try {
//                        writer.write(x + "," + y + "," + velocities.getFirst() + "," + velocities.getSecond() + "\n");
//                    } catch (IOException e) { }
//                });
                writer.close();
            } catch (Exception e) { }
        });
    }

}
