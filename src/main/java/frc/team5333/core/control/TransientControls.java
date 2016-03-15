package frc.team5333.core.control;

import edu.wpi.first.wpilibj.Joystick;
import frc.team5333.core.control.command.CCommandUpperTransfer;
import frc.team5333.core.control.strategy.*;
import frc.team5333.core.systems.Systems;
import frc.team5333.core.vision.VisionFrame;
import frc.team5333.core.vision.VisionNetwork;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * The TransientControls class is used for parts of the Control System that function regardless of state. These controls
 * are mostly setup for Driver Control, such as HotButtons on the Joystick to trigger drive modes, and Joystick
 * configuration.
 *
 * Thanks to this class, hitting the #12 button on the Right joystick at the beginning of a match will automatically
 * configure the joystick handed-ness, in case they are plugged in wrongly. Additionally, it controls buttons for
 * Teleop strategies.
 *
 * @author Jaci
 */
public class TransientControls {

    static ArrayList<TriggerContainer> containers;
    static final Object lock = new Object();

    public static class TriggerContainer {
        public Supplier<Boolean> condition;
        public Runnable run;
    }

    public static void init() {
        containers = new ArrayList<>();

        Joystick lJoy = Operator.getLeftJoystick();
        Joystick rJoy = Operator.getRightJoystick();

        // Hitting the #12 Button on the Right Joystick will auto-configure the handed-ness of the joysticks
        triggerOn(onChangeRising(() -> { return Operator.joy_1.getRawButton(12); }), () -> { Operator.right_is_1 = true; });
        triggerOn(onChangeRising(() -> { return Operator.joy_2.getRawButton(12); }), () -> { Operator.right_is_1 = false; });

        // Hitting the #2 Button on either Joystick will force control to be regained by the driver, in the case
        // an autonomous action is not working properly
        triggerOn(onChangeRising(() -> { return Operator.eitherButton(2); }), () -> {
            StrategyController.INSTANCE.setStrategy(new StrategyOperator()); });

        // Hitting the #4 Button on the Drive Joystick will cause the Robot to auto-align against the Vision Tracking
        // target.
        triggerOn(onChangeRising(() -> { return Systems.control.driveButton(4); }), () -> {
            StrategyController.INSTANCE.setStrategy(new StrategyAlign()); });

        // Hitting the #6 Button on the Shooter Joystick will cause the Robot to shoot a boulder based on the targeting
        // info given by the Vision Tracking System.
        triggerOn(onChangeRising(() -> { return Systems.control.shootButton(6); }), () -> {
            StrategyShoot strat = new StrategyShoot();
            strat.setGenOnEnable(true);
            strat.setInstant(true);
            StrategyController.INSTANCE.setStrategy(strat);
        });

        // Hitting the #3 Button on the Shooter Joystick will toggle the Passive Spinup, something that runs in the background
        // to keep the Flywheels spinning to avoid waiting for a spinup when shooting a boulder.
        triggerOn(onChangeRising(() -> { return Systems.control.shootButton(3); }), () -> {
            Systems.shooter.setPassiveSpinup(!Systems.shooter.getPassiveSpinup());
        });

        // Hitting the #5 Button on the Shooter Joystick will cause a Boulder in the Lower Chamber to be transferred to
        // the Upper Chamber to be held. This is done in preparation for the shooting of a Low Goal.
        triggerOn(onChangeRising(() -> { return Systems.control.shootButton(5); }), () -> {
            new CCommandUpperTransfer().start();
        });
    }

    /**
     * Tick the controls. This is periodically called regardless of state
     */
    public static void tick() {
        synchronized (lock) {
            for (TriggerContainer container : containers)
                if (container.condition.get()) container.run.run();
        }
    }

    /**
     * Register a new trigger. If the condition is true, the runnable callback is called. This is checked once every
     * tick.
     * @param condition A supplier of a Boolean Value representing whether the callback should be called.
     */
    public static void triggerOn(Supplier<Boolean> condition, Runnable callback) {
        synchronized (lock) {
            TriggerContainer container = new TriggerContainer();
            container.condition = condition;
            container.run = callback;
            containers.add(container);
        }
    }

    /**
     * Convert a Boolean Supplier to a Rising-Edge Boolean Supplier. The most obvious use of this is for a button,
     * which will trigger return 'true' when the state of the button goes from unpressed to pressed. This prevents
     * the trigger being detected for the duration of the hold.
     * @param otherSupplier The supplier to filter the rising edge of
     */
    public static Supplier<Boolean> onChangeRising(Supplier<Boolean> otherSupplier) {
        return new Supplier<Boolean>() {
            boolean lastState;
            @Override
            public Boolean get() {
                boolean newState = otherSupplier.get();
                boolean ret = newState != lastState && !lastState;
                lastState = newState;
                return ret;
            }
        };
    }

}
