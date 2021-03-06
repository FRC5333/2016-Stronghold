package frc.team5333.core.hardware;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Talon;
import frc.team5333.core.Core;
import frc.team5333.lib.device.ADIS16448_IMU;
import jaci.openrio.toast.core.Environment;
import jaci.openrio.toast.lib.registry.Registrar;

import java.util.function.Function;

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

    public static CANTalon motor_master_left, motor_slave_left, motor_master_right, motor_slave_right;
    public static CANTalon motor_flywheel_top, motor_flywheel_bottom;
    public static Talon motor_intake;
    public static MotorGroup drive_motors, flywheel_motors, all_motors;
    public static ADIS16448_IMU imu_mxp;

    public static void init() {
        motor_master_left       = Registrar.canTalon(Core.config.getInt("core.io.motor.left_master",        11));
        motor_slave_left        = Registrar.canTalon(Core.config.getInt("core.io.motor.left_slave",         10));
        motor_master_right      = Registrar.canTalon(Core.config.getInt("core.io.motor.right_master",       13));
        motor_slave_right       = Registrar.canTalon(Core.config.getInt("core.io.motor.right_slave",        12));

        motor_flywheel_top      = Registrar.canTalon(Core.config.getInt("core.io.motor.flywheel_top",       14));
        motor_flywheel_bottom   = Registrar.canTalon(Core.config.getInt("core.io.motor.flywheel_bottom",    15));

        motor_intake            = Registrar.talon(Core.config.getInt("core.io.motor.intake",                0));

        drive_motors    = new MotorGroup(motor_master_left, motor_slave_left, motor_master_right, motor_slave_right);
        flywheel_motors = new MotorGroup(motor_flywheel_bottom, motor_flywheel_top);
        all_motors      = new MotorGroup(drive_motors, flywheel_motors, motor_intake);

        if (IMU_SUPPORTED()) imu_mxp = new ADIS16448_IMU();
    }

    public static double maybeIMU(Function<ADIS16448_IMU, Double> d) {
        if (IMU_SUPPORTED()) return d.apply(imu_mxp);
        return 0.0;
    }

    public static boolean IMU_SUPPORTED() {
        return Environment.isEmbedded();
    }

    public static void setLeftMotors(double speed) {
        motor_master_left.set(speed);
        motor_slave_left.set(speed);
    }

    public static void setRightMotors(double speed) {
        motor_master_right.set(speed);
        motor_slave_right.set(speed);
    }

}
