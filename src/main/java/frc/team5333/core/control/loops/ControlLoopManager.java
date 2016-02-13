package frc.team5333.core.control.loops;

import frc.team5333.core.Core;
import frc.team5333.core.control.strategy.StrategyController;

public class ControlLoopManager {

    public static int RATE = 200;
    private static int wait_period;
    public static Thread thread;

    public static void init() {
        RATE = Core.config.getInt("control.loop.rate", 200);
        wait_period = 1000 / 200;
        thread = new Thread(() -> {
            while (true) {
                tick();
                try {
                    Thread.sleep(wait_period);
                } catch (InterruptedException e) { }
            }
        });
    }

    public static void tick() {
        StrategyController.INSTANCE.tickFast();
    }

}
