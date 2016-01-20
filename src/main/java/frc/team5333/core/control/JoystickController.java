package frc.team5333.core.control;

import edu.wpi.first.wpilibj.Joystick;
import jaci.openrio.toast.lib.module.ModuleConfig;

/**
 * This wrapper class makes sure the bindings to our Joystick Axis/Buttons can be changed from one place, instead
 * of refactoring code. This reads from a configuration file to make sure that if the model of Joystick breaks and/or
 * is changed, the Robot is still operational with a few changes of a config file.
 *
 * @author Jaci
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
        idTwist     = joyConfig.getInt("joy.id.twist", 2);
        idSlider    = joyConfig.getInt("joy.id.slider", 3);
    }

    public static boolean getTrigger(Joystick joy) {
        return joy.getRawButton(idTrigger);
    }

    public static boolean getBumper(Joystick joy) {
        return joy.getRawButton(idBumper);
    }

    public static double getX(Joystick joy) {
        return joy.getRawAxis(idX);
    }

    public static double getY(Joystick joy) {
        return joy.getRawAxis(idY);
    }

    public static double getTwist(Joystick joy) {
        return joy.getRawAxis(idTwist);
    }

    public static double getSlider(Joystick joy) {
        return joy.getRawAxis(idSlider);
    }
}
