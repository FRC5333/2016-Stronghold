package frc.team5333.core.control.strategy

import edu.wpi.first.wpilibj.CANTalon
import frc.team5333.core.Core
import frc.team5333.core.control.ControlLease
import frc.team5333.core.control.profiling.SplineFollower
import frc.team5333.core.control.profiling.SplineSystem
import frc.team5333.core.hardware.IO
import frc.team5333.core.systems.DriveSystem
import frc.team5333.core.systems.Systems
import frc.team5333.lib.util.MathUtil
import jaci.openrio.toast.lib.math.MathHelper

class StrategyMotionProfile(var trajectory: Pair<SplineSystem.Trajectory, SplineSystem.Trajectory>) : Strategy() {

    lateinit var lease_drive: ControlLease.Lease<DriveSystem>

    override fun getName(): String = "Motion Profile"

    var complete = false
    var followerLeft: SplineFollower = SplineFollower()
    var followerRight: SplineFollower = SplineFollower()

    override fun onEnable() {
        super.onEnable()

        var velocity_actual = Core.config.getFloat("motion.max_velocity_actual", 1.7f)
        var velocity = Core.config.getFloat("motion.max_velocity", 1.2f)
        var prop = Core.config.getDouble("motion.profile.p", 0.8)
        var deriv = Core.config.getDouble("motion.profile.d", 0.0)
        var accel = Core.config.getDouble("motion.profile.a", 0.0)

        var adj_scaler = (1.0 * (velocity / velocity_actual)) / velocity

        followerLeft.configurePID_VA(prop, 0.0, deriv, adj_scaler, accel)
        followerRight.configurePID_VA(prop, 0.0, deriv, adj_scaler, accel)

        IO.motor_master_left.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 20)
        IO.motor_master_right.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 20)

        followerLeft.configureEncoder(-IO.motor_master_left.encPosition, 1000, 0.08)
        followerRight.configureEncoder(IO.motor_master_right.encPosition, 1000, 0.08)

        followerLeft.trajectory = trajectory.first
        followerRight.trajectory = trajectory.second

        lease_drive = Systems.drive.LEASE.acquire(ControlLease.Priority.HIGHER)
    }

    override fun onDisable() {
        super.onDisable()
        lease_drive.release()

        IO.motor_master_left.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 100)
        IO.motor_master_right.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 100)
    }

    override fun tick() {

    }

    override fun tickFast() {
        lease_drive.use {
            var l = -followerLeft.calculate(-it.leftMotor.encPosition)
            var r = followerRight.calculate(it.rightMotor.encPosition)

            var imu_heading = MathUtil.boundHalfDeg(IO.maybeIMU { it.angleY })
            var desired_heading = MathUtil.boundHalfDeg(MathHelper.r2d(followerLeft.heading))

            var angleDiff = MathUtil.boundHalfDeg(desired_heading - imu_heading)

            var turn = 0.8 * (-1.0/80.0) * angleDiff

            it.leftMotor.set(l + turn)
            it.rightMotor.set(r - turn)
        }
    }

    override fun isOperatorControl(): Boolean = false

    override fun isFast(): Boolean = true

    override fun isComplete(): Boolean = followerLeft.finished() && followerRight.finished()
}