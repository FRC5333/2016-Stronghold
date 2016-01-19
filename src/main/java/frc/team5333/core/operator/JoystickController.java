package frc.team5333.core.operator;

import jaci.openrio.toast.lib.module.ModuleConfig;

/**
 * This wrapper class makes sure the bindings to our Joystick Axis/Buttons can be changed from one place, instead
 * of refactoring code. This reads from a configuration file.
 */
public class JoystickController {

    public static ModuleConfig joyConfig;

    static int idTrigger, idBumper, idX, idY, idTwist, idSlider;

    public static void init() {
        joyConfig   = new ModuleConfig("Joystick");
        idTrigger   = joyConfig.getInt("joy.id.trigger", 1);
        idBumper    = joyConfig.getInt("joy.id.bumper", 2);
        idX         = joyConfig.getInt("joy.id.x", 0);
        idY         = joyConfig.getInt("joy.id.y", 1);
        idTwist     = joyConfig.getInt("joy.id.twist", 3);
        idSlider    = joyConfig.getInt("joy.id.slider", 2);
    }

}
