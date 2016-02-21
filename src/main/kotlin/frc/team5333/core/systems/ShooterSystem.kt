package frc.team5333.core.systems

import edu.wpi.first.wpilibj.CANTalon
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.Talon
import frc.team5333.core.control.ControlLease
import frc.team5333.core.control.Joy
import frc.team5333.core.control.Operator
import frc.team5333.core.events.ShootEvent
import frc.team5333.lib.events.EventBus
import java.util.function.Consumer
import frc.team5333.lib.util.Range
import kotlin.ranges.rangeTo

class ShooterSystem(var flywheelTop: CANTalon, var flywheelBottom: CANTalon, var intake: Talon) {
    val LEASE = ControlLease(this)

    // AUTOMATIC CONTROL

//    fun calculateSpeeds(relative_distance: Double): Pair<Double, Double> {

//    }

    // MANUAL CONTROL

    fun runFlywheels() {
        var pairs = getFlywheelPairs()
        flywheelTop.set(pairs.first)
        flywheelBottom.set(pairs.first)
        intake.set(pairs.second)
    }

    fun getFlywheelPairs(): Pair<Double, Double> {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()
        var mode = Systems.control.driveMode()

        if (mode == ControlSystem.DriveMode.LEFT_ONLY) {
            return calc(lJoy);
        } else if (mode == ControlSystem.DriveMode.RIGHT_ONLY) {
            return calc(rJoy);
        } else {
            return Pair(0.0, 0.0)
        }
    }

    internal fun calc(joy: Joystick): Pair<Double, Double> {
        return Pair(sq(joy.getY()), sq(Joy.getX(joy)))
    }

    internal fun sq(value: Double): Double {
        if (value >= 0.0)
            return value * value
        else
            return -(value * value)
    }

}