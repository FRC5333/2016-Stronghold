package frc.team5333.core;

import edu.wpi.first.wpilibj.Joystick;

public class Operator {

    public static Joystick left_joy, right_joy;

    public static void init() {
        left_joy    = new Joystick(Core.config.getInt("core.operator.joy.left",     1));
        right_joy   = new Joystick(Core.config.getInt("core.operator.joy.right",    2));
    }

}
