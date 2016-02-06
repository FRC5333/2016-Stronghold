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

    var throttleCoefficient = 0.5
    var throttleScale = 1.0
    var inverted = false

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
     * Do a single update. This is mostly used for setting the ThrottleScale based on the inputs on the Joystick
     */
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
            return jaciDrive3(sq(-Joy.getY(lJoy)), sq(-Joy.getX(lJoy)), Joy.getTwist(lJoy)).first
        else if (driveMode() == DriveMode.RIGHT_ONLY)
            return jaciDrive3(sq(-Joy.getY(rJoy)), sq(-Joy.getX(rJoy)), Joy.getTwist(rJoy)).first
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
            return jaciDrive3(sq(-Joy.getY(lJoy)), sq(-Joy.getX(lJoy)), Joy.getTwist(lJoy)).second
        else if (driveMode() == DriveMode.RIGHT_ONLY)
            return jaciDrive3(sq(-Joy.getY(rJoy)), sq(-Joy.getX(rJoy)), Joy.getTwist(rJoy)).second
        return 0.0
    }

    /**
     * Calculate the Left/Right sides of the Drive Train using the Throttle and Rotate values (usually
     * X/Y Axis) of the Joystick. On it's own, the X-axis has a limited maximum rotation. This makes it more accurate
     * in small amounts, but adding the twist of the joystick to this equation enables a fast turn.
     *
     * This is called the Jaci drive because apparently I'm an egotistical prick
     */
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