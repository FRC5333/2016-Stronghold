package frc.team5333.core.systems

import edu.wpi.first.wpilibj.CANTalon
import frc.team5333.core.control.ControlLease
import frc.team5333.core.control.Joy
import frc.team5333.core.control.Operator
import frc.team5333.core.events.ShootEvent
import frc.team5333.lib.events.EventBus
import java.util.function.Consumer
import frc.team5333.lib.util.Range
import kotlin.ranges.rangeTo

class ShooterSystem(var flywheelTop: CANTalon, var flywheelBottom: CANTalon) {
    val LEASE = ControlLease(this)

    fun runFlywheels() {
        var pairs = getFlywheelPairs()
        flywheelTop.set(pairs.first)
        flywheelBottom.set(pairs.second)
    }

    fun getFlywheelPairs(): Pair<Double, Double> {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()
        var mode = Systems.control.driveMode()

        if (mode == ControlSystem.DriveMode.LEFT_ONLY) {
            return calculateRatio(Joy.getX(rJoy), -Joy.getY(rJoy))
        } else if (mode == ControlSystem.DriveMode.RIGHT_ONLY) {
            return calculateRatio(Joy.getX(lJoy), -Joy.getY(lJoy))
        } else {
            return Pair(0.0, 0.0)
        }
    }

    internal fun calculateRatio(throttle: Double, ratio: Double): Pair<Double, Double> {
        var top = 0.0
        var btm = 0.0
        if (throttle > 0.0) {
            if (ratio > 0.0) {
                top = throttle - ratio
                btm = Math.max(throttle, ratio)
            } else {
                top = Math.max(throttle, -ratio)
                btm = throttle + ratio
            }
        } else {
            if (ratio > 0.0) {
                top = -Math.max(-throttle, ratio)
                btm = throttle + ratio
            } else {
                top = throttle - ratio
                btm = -Math.max(-throttle, -ratio)
            }
        }
        return Pair(top, btm)
    }

}