package frc.team5333.core.control;

import edu.wpi.first.wpilibj.Joystick;
import frc.team5333.core.Core;

/**
 * The Operator Class handles all the Operator Inputs on the Driver Station. In this particular class, the Joysticks
 * can be configured as Right or Left. This is to make sure joysticks do not get confused with one another.
 *
 * @author Jaci
 */
public class Operator {

    public static Joystick joy_1, joy_2;
    public static boolean right_is_1 = false;

    public static void init() {
        JoystickController.init();
        joy_1    = new Joystick(Core.config.getInt("core.operator.joy.one", 0));
        joy_2    = new Joystick(Core.config.getInt("core.operator.joy.two", 1));
    }

    public static Joystick getLeftJoystick() {
        return right_is_1 ? joy_2 : joy_1;
    }

    public static Joystick getRightJoystick() {
        return right_is_1 ? joy_1 : joy_2;
    }

    public static void switchJoysticks() {
        right_is_1 = !right_is_1;
    }

}
