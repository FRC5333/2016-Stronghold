package frc.team5333.core.operator;

import edu.wpi.first.wpilibj.Joystick;
import frc.team5333.core.Core;

public class Operator {

    public static Joystick joy_1, joy_2;
    public static boolean joy_reversed = false;

    public static void init() {
        joy_1    = new Joystick(Core.config.getInt("core.operator.joy.one", 1));
        joy_2    = new Joystick(Core.config.getInt("core.operator.joy.two", 2));
    }

    public static Joystick getLeftJoystick() {
        return joy_reversed ? joy_2 : joy_1;
    }

    public static Joystick getRightJoystick() {
        return joy_reversed ? joy_1 : joy_2;
    }

    public static void toggleReverse() {
        joy_reversed = !joy_reversed;
    }

}
