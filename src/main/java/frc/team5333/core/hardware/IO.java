package frc.team5333.core.hardware;

import edu.wpi.first.wpilibj.CANTalon;
import frc.team5333.core.Core;
import frc.team5333.lib.device.ADIS16448_IMU;
import jaci.openrio.toast.lib.registry.Registrar;

/**
 * The IO Class is used to specify all the Inputs and Outputs of the RoboRIO. Things like Motors, Switches and LEDs go
 * in here.
 *
 * Some motors are repeated under the MotorGroup class. This is so that groups of motors can be set using a single
 * instance, making setting all motors to a value (such as 0.0) very simple.
 *
 * @author Jaci
 */
public class IO {

    public static CANTalon motor_left_1, motor_left_2, motor_right_1, motor_right_2;
    public static MotorGroup drive_motors, all_motors;
    public static ADIS16448_IMU imu_mxp;

    public static void init() {
        motor_left_1    = Registrar.canTalon(Core.config.getInt("core.io.motor.left_1",   10));
        motor_left_2    = Registrar.canTalon(Core.config.getInt("core.io.motor.left_2",   11));
        motor_right_1   = Registrar.canTalon(Core.config.getInt("core.io.motor.right_1",  12));
        motor_right_2   = Registrar.canTalon(Core.config.getInt("core.io.motor.right_2",  13));

        drive_motors    = new MotorGroup(motor_left_1, motor_left_2, motor_right_1, motor_right_2);
        all_motors      = new MotorGroup(drive_motors);

        imu_mxp = new ADIS16448_IMU();
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
