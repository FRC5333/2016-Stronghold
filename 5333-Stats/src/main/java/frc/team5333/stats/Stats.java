package frc.team5333.stats;

import jaci.openrio.toast.core.loader.annotation.Priority;
import jaci.openrio.toast.lib.module.IterativeModule;

public class Stats extends IterativeModule {

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
        Test.init();
    }
}
