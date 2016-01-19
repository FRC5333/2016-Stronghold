package frc.team5333.core.control;

import edu.wpi.first.wpilibj.Joystick;

import java.util.ArrayList;
import java.util.function.Supplier;

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
        triggerOn(() -> { return Operator.joy_1.getRawButton(12); }, () -> { Operator.right_is_1 = true; });
        triggerOn(() -> { return Operator.joy_2.getRawButton(12); }, () -> { Operator.right_is_1 = false; });
    }

    public static void tick() {
        synchronized (lock) {
            for (TriggerContainer container : containers)
                if (container.condition.get()) container.run.run();
        }
    }

    public static void triggerOn(Supplier<Boolean> condition, Runnable callback) {
        synchronized (lock) {
            TriggerContainer container = new TriggerContainer();
            container.condition = condition;
            container.run = callback;
            containers.add(container);
        }
    }

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
