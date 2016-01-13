package frc.team5333.core;

import edu.wpi.first.wpilibj.CANTalon;
import jaci.openrio.toast.lib.registry.Registrar;

public class IO {

    public static CANTalon  motor_left, motor_right;

    public static void init() {
        motor_left  = Registrar.canTalon(Core.config.getInt("core.io.motor.left",   1));
        motor_right = Registrar.canTalon(Core.config.getInt("core.io.motor.right",  2));
    }

}
