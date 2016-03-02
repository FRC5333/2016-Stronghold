package frc.team5333.auto;

import frc.team5333.core.Core;
import jaci.openrio.toast.core.loader.annotation.Priority;
import jaci.openrio.toast.core.thread.Async;
import jaci.openrio.toast.lib.module.IterativeModule;

public class AutonomousModule extends IterativeModule {

    @Override
    public String getModuleName() {
        return "5333-Autonomous";
    }

    @Override
    public String getModuleVersion() {
        return "0.1.0";
    }

    @Override
    @Priority(level = Priority.Level.LOWEST)
    public void robotInit() {
        // Do this async so that if we're waiting for the Coprocessor to generate motion profiles, we don't block the main thread
        Async.schedule(AutonomousLoader::load);
    }

    @Override
    public void autonomousInit() {
        AutonomousBase active = AutonomousLoader.getActive();
        if (active == null) {
            Core.logger.error("No Active Autonomous Program!");
        } else {
            active.run();
        }
    }
}
