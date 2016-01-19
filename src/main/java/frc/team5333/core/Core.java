package frc.team5333.core;

import frc.team5333.core.data.MatchInfo;
import frc.team5333.core.operator.Operator;
import jaci.openrio.toast.core.loader.annotation.Priority;
import jaci.openrio.toast.lib.log.Logger;
import jaci.openrio.toast.lib.module.IterativeModule;
import jaci.openrio.toast.lib.module.ModuleConfig;

public class Core extends IterativeModule {

    public static ModuleConfig config;
    public static Logger logger;

    @Override
    public String getModuleName() {
        return "5333-Core";
    }

    @Override
    public String getModuleVersion() {
        return "0.1.0";
    }

    @Override
    @Priority(level = Priority.Level.HIGHEST)
    public void prestart() {
        logger = new Logger("5333-Stronghold", Logger.ATTR_DEFAULT);
        config = new ModuleConfig("5333-Stronghold");
        Operator.init();
        IO.init();

        MatchInfo.init();
    }
}
