package frc.team5333.core.hardware;

import edu.wpi.first.wpilibj.CANTalon;
import frc.team5333.core.Core;
import jaci.openrio.toast.lib.registry.Registrar;

public class IO {

    public static CANTalon motor_left_1, motor_left_2, motor_right_1, motor_right_2;
    public static MultiMotor drive_motors, all_motors;

    public static void init() {
        motor_left_1    = Registrar.canTalon(Core.config.getInt("core.io.motor.left_1",   1));
        motor_left_2    = Registrar.canTalon(Core.config.getInt("core.io.motor.left_2",   2));
        motor_right_1   = Registrar.canTalon(Core.config.getInt("core.io.motor.right_1",  3));
        motor_right_2   = Registrar.canTalon(Core.config.getInt("core.io.motor.right_2",  4));

        drive_motors    = new MultiMotor(motor_left_1, motor_left_2, motor_right_1, motor_right_2);
        all_motors      = new MultiMotor(drive_motors);
    }

    public static void setLeftMotors(double speed) {
        motor_left_1.set(speed);
        motor_left_2.set(speed);
    }

    public static void setRightMotors(double speed) {
        motor_right_1.set(speed);
        motor_right_2.set(speed);
    }

}
