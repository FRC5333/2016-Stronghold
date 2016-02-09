package frc.team5333.core;

import frc.team5333.core.commands.CommandClearConfigs;
import frc.team5333.core.commands.ShooterCommand;
import frc.team5333.core.control.Operator;
import frc.team5333.core.control.TransientControls;
import frc.team5333.core.data.MatchInfo;
import frc.team5333.core.hardware.IO;
import frc.team5333.core.network.NetworkHub;
import frc.team5333.core.teleop.TeleopController;
import jaci.openrio.toast.core.StateTracker;
import jaci.openrio.toast.core.command.CommandBus;
import jaci.openrio.toast.core.loader.annotation.Priority;
import jaci.openrio.toast.lib.log.Logger;
import jaci.openrio.toast.lib.module.IterativeModule;
import jaci.openrio.toast.lib.module.ModuleConfig;

/**
 * The Core class of Team 5333's Stronghold code. This class serves to startup other parts of the code, as well
 * as delegate periodic functions to the appropriate handler.
 *
 * @author Jaci
 */
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

        NetworkHub.INSTANCE.start();

        Operator.init();
        IO.init();

        MatchInfo.load();

        TransientControls.init();
        StateTracker.addTicker((s) -> { TransientControls.tick(); });

        CommandBus.registerCommand(new ShooterCommand());
        CommandBus.registerCommand(new CommandClearConfigs());
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        TeleopController.INSTANCE.tick();
    }

}