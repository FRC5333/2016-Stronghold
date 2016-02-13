package frc.team5333.core.control;

import edu.wpi.first.wpilibj.Notifier;
import frc.team5333.core.control.strategy.StrategyController;

public class ControlLoopManager implements Runnable {

    boolean firstRun = true;

    @Override
    public void run() {
        if (firstRun) {
            Thread.currentThread().setPriority(6);
            firstRun = false;
        }
        StrategyController.INSTANCE.tickFast();
    }

    public static Notifier notifier;

    public static void start() {
        notifier = new Notifier(new ControlLoopManager());
        notifier.startPeriodic(0.01);
    }

}
