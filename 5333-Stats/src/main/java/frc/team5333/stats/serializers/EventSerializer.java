package frc.team5333.stats.serializers;

import frc.team5333.core.data.MatchInfo;
import frc.team5333.lib.events.EventBase;
import frc.team5333.lib.events.EventBus;
import frc.team5333.lib.events.EventListener;
import frc.team5333.lib.events.EventPriority;
import jaci.openrio.toast.core.Toast;
import jaci.openrio.toast.core.io.Storage;
import jaci.openrio.toast.core.thread.Async;

import java.io.*;

public class EventSerializer {

    static File file;
    static FileWriter writer;

    public static void init() {
        try {
            file = Storage.highestPriority("system/stats/events/" + MatchInfo.matchType.getShortName() + "_" + MatchInfo.matchNum + ".csv");
            file.getParentFile().mkdirs();
            writer = new FileWriter(file);
            writer.write("time,matchtime,eventname,classname,cancelled,successful,customdata\n");
            writer.flush();
        } catch (IOException e) {}
        EventBus.INSTANCE.register(EventSerializer.class);
    }

    @EventListener(allowCancelled = true, respectsInheritance = true, priority = EventPriority.LOWEST)
    public static void onEvent(EventBase base) {
        Async.schedule(() -> {
            try {
                if (writer != null) {
                    writer.write(String.format("%d,%f,%s,%s,%s,%s,%s\n", base.raiseTime, base.matchTimeStart,
                            base.getClass().getSimpleName(), base.getClass().getCanonicalName(),
                            base.isCancelled() ? "true" : "false", base.isCancelled() ? "false" : "true", base.getCustomData()));
                    writer.flush();
                }
            } catch (Exception e) { }
        });

    }

}
