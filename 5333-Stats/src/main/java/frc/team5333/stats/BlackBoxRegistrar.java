package frc.team5333.stats;

import frc.team5333.core.hardware.IO;
import jaci.openrio.module.blackbox.BlackBoxContext;
import jaci.openrio.toast.core.StateTracker;
import jaci.openrio.toast.lib.state.RobotState;

import java.util.function.Supplier;

public class BlackBoxRegistrar {

    public static void register(BlackBoxContext ctx) {
        ctx.add("disabled", to(() -> { return StateTracker.currentState == RobotState.DISABLED; }));
        ctx.add("autonomous", to(() -> { return StateTracker.currentState == RobotState.AUTONOMOUS; }));
        ctx.add("teleoperated", to(() -> { return StateTracker.currentState == RobotState.TELEOP; }));

        ctx.add("motor_left", IO.motor_master_left::get);
        ctx.add("motor_right", IO.motor_master_right::get);

        ctx.add("motor_left_encoder_velocity", IO.motor_master_left::getEncVelocity);
        ctx.add("motor_right_encoder_velocity", IO.motor_slave_right::getEncVelocity);
        ctx.add("motor_left_encoder_position", IO.motor_master_left::getEncPosition);
        ctx.add("motor_right_encoder_position", IO.motor_slave_right::getEncPosition);

        if (IO.IMU_SUPPORTED()) {
            ctx.add("imu_accel_x", IO.imu_mxp::getAccelX);
            ctx.add("imu_accel_y", IO.imu_mxp::getAccelY);
            ctx.add("imu_accel_z", IO.imu_mxp::getAccelZ);

            ctx.add("imu_angle_x", IO.imu_mxp::getAngleX);
            ctx.add("imu_angle_y", IO.imu_mxp::getAngleY);
            ctx.add("imu_angle_z", IO.imu_mxp::getAngleZ);

            ctx.add("imu_rate_x", IO.imu_mxp::getRateX);
            ctx.add("imu_rate_y", IO.imu_mxp::getRateY);
            ctx.add("imu_rate_z", IO.imu_mxp::getRateZ);

            ctx.add("imu_barometric_pressure", IO.imu_mxp::getBarometricPressure);

            ctx.add("imu_mag_x", IO.imu_mxp::getMagX);
            ctx.add("imu_mag_y", IO.imu_mxp::getMagY);
            ctx.add("imu_mag_z", IO.imu_mxp::getMagZ);

            ctx.add("imu_pitch", IO.imu_mxp::getPitch);
            ctx.add("imu_yaw", IO.imu_mxp::getYaw);
            ctx.add("imu_roll", IO.imu_mxp::getRoll);

            ctx.add("imu_temperature", IO.imu_mxp::getTemperature);

            ctx.add("imu_quarternion_w", IO.imu_mxp::getQuaternionW);
            ctx.add("imu_quarternion_x", IO.imu_mxp::getQuaternionX);
            ctx.add("imu_quarternion_y", IO.imu_mxp::getQuaternionY);
            ctx.add("imu_quarternion_z", IO.imu_mxp::getQuaternionZ);
        }
    }

    public static Supplier<Number> to(Supplier<Boolean> src) {
        return () -> { return src.get() ? 1 : 0; };
    }

}
