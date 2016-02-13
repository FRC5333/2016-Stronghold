package frc.team5333.core.control.strategy

import edu.wpi.first.wpilibj.CANTalon
import frc.team5333.core.Core
import frc.team5333.core.hardware.IO
import frc.team5333.core.systems.SplineFollower
import frc.team5333.core.systems.SplineSystem
import jaci.openrio.toast.core.shared.GlobalBlackboard
import kotlin.collections.forEach

class StrategyMotionProfile(var trajectory: Pair<SplineSystem.Trajectory, SplineSystem.Trajectory>) : Strategy() {

    override fun getName(): String = "Motion Profile"

    var complete = false
    var followerLeft: SplineFollower = SplineFollower()
    var followerRight: SplineFollower = SplineFollower()

    override fun onEnable() {
        super.onEnable()
        followerLeft.configurePID_VA(0.8, 0.005, 0.01, 0.1, 0.1)
        followerRight.configurePID_VA(0.8, 0.005, 0.01, 0.1, 0.1)

        followerLeft.configureEncoder(IO.motor_master_left.encPosition, 1024, 0.089)
        followerRight.configureEncoder(IO.motor_master_right.encPosition, 1024, 0.089)
    }

    override fun onDisable() {
        super.onDisable()
    }

    override fun tick() {

    }

    override fun tickFast() {
        IO.motor_master_left.set(followerLeft.calculate(IO.motor_master_left.encPosition))
        IO.motor_master_right.set(followerRight.calculate(IO.motor_master_right.encPosition))
    }

    override fun isOperatorControl(): Boolean = false

    override fun isFast(): Boolean = true

    override fun isComplete(): Boolean = followerLeft.finished() && followerRight.finished()
}