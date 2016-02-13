package frc.team5333.stats;

import frc.team5333.core.data.MatchInfo;
import frc.team5333.stats.serializers.EventSerializer;
import frc.team5333.stats.serializers.ShooterSerializer;
import frc.team5333.stats.serializers.SplineSerializer;
import jaci.openrio.module.blackbox.BlackBox;
import jaci.openrio.module.blackbox.BlackBoxContext;
import jaci.openrio.toast.core.loader.annotation.Priority;
import jaci.openrio.toast.lib.module.IterativeModule;

public class Stats extends IterativeModule {

    public static BlackBoxContext ctx;
    static boolean startedOperation = false;

    @Override
    public String getModuleName() {
        return "5333-Stats";
    }

    @Override
    public String getModuleVersion() {
        return "0.1.0";
    }

    @Override
    @Priority(level = Priority.Level.LOWEST)
    public void prestart() {
    }

    public void maybeStart() {
        if (!startedOperation) {
            ctx = BlackBox.context(MatchInfo.matchType.getShortName() + "_" + MatchInfo.matchNum);

            BlackBoxRegistrar.register(ctx);
            ShooterSerializer.init();
            SplineSerializer.init();
            EventSerializer.init();
        }
        startedOperation = true;
    }

    public void autonomousInit() {
        maybeStart();
    }

    public void autonomousPeriodic() {
        if (ctx != null) ctx.tick();
    }

    public void teleopPeriodic() {
        if (ctx != null) ctx.tick();
    }

    public void teleopInit() {
        maybeStart();
    }

}
