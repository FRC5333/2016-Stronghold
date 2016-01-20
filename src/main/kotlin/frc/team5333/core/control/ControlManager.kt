package frc.team5333.core.control

/**
 * The ControlManager class is the interpreter for inputs coming from the operator; turning them into values
 * that can be easily read by Teleop Strategies or other software components.
 *
 * This class has 2 types of mode: Single Stick and Dual Stick. The Single Stick mode uses a single joystick
 * (whichever has the trigger pushed, or the right one by default) to drive the base similar to RobotDrive's
 * arcadeDrive() method. If the triggers on both sticks are pushed simultaneously, the Y-axis on each stick is
 * used to drive its respective side of the drive train. These two modes enable a large granularity of control
 * when using both sticks, but still allows for drive control while using the secondary joystick for the super
 * structure or other tasks. More of this will be documented in the Control System Documentation
 *
 * @author Jaci
 */
public enum class ControlManager {
    INSTANCE;       // Kotlin doesn't have statics, so a singleton will have to do

    enum class DriveMode {
        LEFT_ONLY, RIGHT_ONLY, BOTH
    }

    /**
     * Gets the DriveMode of the control system based on the triggers of the left and right joysticks.
     * This is used to determine how controls are interpreted.
     */
    fun driveMode(): DriveMode {
        val lJoy = Operator.getLeftJoystick()
        val rJoy = Operator.getRightJoystick()

        if (lJoy.trigger && rJoy.trigger)
            return DriveMode.BOTH
        else if (lJoy.trigger)
            return DriveMode.LEFT_ONLY
        else
            return DriveMode.RIGHT_ONLY
    }

    /**
     * Get the Left Side of the Drive Train using the current Drive Mode. In Single Stick mode,
     * This is the result of calculateThrottlePair() using the X and Y axis of the specified Joystick.
     * In Dual Stick mode, this is the Y Axis of the left stick.
     */
    fun getLeftDrive(): Double {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()

        if (driveMode() == DriveMode.BOTH)
            return sq(-Joy.getY(lJoy))
        else if (driveMode() == DriveMode.LEFT_ONLY)
            return calculateThrottlePair(sq(-Joy.getY(lJoy)), sq(Joy.getX(lJoy))).first
        else if (driveMode() == DriveMode.RIGHT_ONLY)
            return calculateThrottlePair(sq(-Joy.getY(rJoy)), sq(Joy.getX(rJoy))).first
        return 0.0
    }

    /**
     * Get the Right Side of the Drive Train using the current Drive Mode. In Single Stick mode,
     * This is the result of calculateThrottlePair() using the X and Y axis of the specified Joystick.
     * In Dual Stick mode, this is the Y Axis of the right stick.
     */
    fun getRightDrive(): Double {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()

        if (driveMode() == DriveMode.BOTH)
            return sq(-Joy.getY(rJoy))
        else if (driveMode() == DriveMode.LEFT_ONLY)
            return calculateThrottlePair(sq(-Joy.getY(lJoy)), sq(Joy.getX(lJoy))).second
        else if (driveMode() == DriveMode.RIGHT_ONLY)
            return calculateThrottlePair(sq(-Joy.getY(rJoy)), sq(Joy.getX(rJoy))).second
        return 0.0
    }

    /**
     * Calculate the Left/Right sides of the Drive Train using the Throttle and Rotate values (usually
     * X/Y Axis) of the Joystick. This is mercilessly stolen from RobotDrive's arcadeDrive() method.
     */
    internal fun calculateThrottlePair(throttle: Double, rotate: Double): Pair<Double, Double> {
        var left = 0.0
        var right = 0.0
        if (throttle > 0.0) {
            if (rotate > 0.0) {
                left = throttle - rotate
                right = Math.max(throttle, rotate);
            } else {
                left = Math.max(throttle, -rotate);
                right = throttle + rotate;
            }
        } else {
            if (rotate > 0.0) {
                left = -Math.max(-throttle, rotate);
                right = throttle + rotate
            } else {
                left = throttle - rotate;
                right = -Math.max(-throttle, -rotate);
            }
        }
        return Pair(left, right)
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