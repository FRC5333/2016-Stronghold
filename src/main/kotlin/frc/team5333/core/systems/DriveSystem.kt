package frc.team5333.core.systems

import edu.wpi.first.wpilibj.CANTalon
import frc.team5333.core.control.ControlLease
import frc.team5333.core.control.Joy
import frc.team5333.core.control.Operator

class DriveSystem(var leftMotor: CANTalon, var rightMotor: CANTalon) {
    val LEASE = ControlLease(this)

    var throttleCoefficient = 0.5
    var throttleScale = 1.0
    var inverted = false

    fun update() {
        var pov = Operator.getRightJoystick().pov
        inverted = Joy.getBumper(Operator.getRightJoystick())
        if (pov == 0 || pov == 360)
            throttleScale = 1.0
        else if (pov == 90)
            throttleScale = 0.75
        else if (pov == 180)
            throttleScale = 0.5
        else if (pov == 270)
            throttleScale = 0.25
    }

    fun tick() {  }

    fun drive() {
        update()
        var pairs = getDrivePairs()
        drive(pairs.first, pairs.second)
    }

    fun drive(l: Double, r: Double) {
        leftMotor.set(l)
        rightMotor.set(-r)
    }

    fun getDrivePairs(): Pair<Double, Double> {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()
        var mode = Systems.control.driveMode()

        if (mode == ControlSystem.DriveMode.BOTH)
            return Pair(sq(Joy.getY(rJoy)), sq(Joy.getY(lJoy)))
        else if (mode == ControlSystem.DriveMode.LEFT_ONLY)
            return jaciDrive3(sq(Joy.getY(lJoy)), sq(-Joy.getX(lJoy)), Joy.getTwist(lJoy))
        else if (mode == ControlSystem.DriveMode.RIGHT_ONLY)
            return jaciDrive3(sq(Joy.getY(rJoy)), sq(-Joy.getX(rJoy)), Joy.getTwist(rJoy))
        return Pair(0.0, 0.0)
    }

    internal fun jaciDrive3(throttle: Double, rotate: Double, twist: Double): Pair<Double, Double> {
        var adjustedRotation = (rotate * throttleCoefficient) + (rotate * twist * twist * (1 - throttleCoefficient))
        var adjustedThrottle = throttle
        if (inverted) adjustedThrottle *= -1

        var left = 0.0
        var right = 0.0
        if (adjustedThrottle > 0.0) {
            if (adjustedRotation > 0.0) {
                left = adjustedThrottle - adjustedRotation
                right = Math.max(adjustedThrottle, adjustedRotation);
            } else {
                left = Math.max(adjustedThrottle, -adjustedRotation);
                right = adjustedThrottle + adjustedRotation;
            }
        } else {
            if (adjustedRotation > 0.0) {
                left = -Math.max(-adjustedThrottle, adjustedRotation);
                right = adjustedThrottle + adjustedRotation
            } else {
                left = adjustedThrottle - adjustedRotation;
                right = -Math.max(-adjustedThrottle, -adjustedRotation);
            }
        }
        return Pair(left * throttleScale, right * throttleScale)
    }

    /**
     * Square value while maintaining the sign. This is used as a filter on the joystick inputs to make it easier for
     * the driver to control.
     */
    internal fun sq(value: Double): Double {
        if (value >= 0.0)
            return value * value
        else
            return -(value * value)
    }

}