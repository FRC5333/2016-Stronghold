package frc.team5333.core.operator

public enum class ControlManager {
    INSTANCE;       // Kotlin doesn't have statics, so a singleton will have to do

    enum class DriveMode {
        LEFT_ONLY, RIGHT_ONLY, BOTH
    }

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

    fun getLeftDrive(): Double {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()

        if (driveMode() == DriveMode.BOTH)
            return -JoystickController.getY(lJoy)
        else if (driveMode() == DriveMode.LEFT_ONLY)
            return calculateThrottlePair(-JoystickController.getY(lJoy), JoystickController.getTwist(lJoy)).first
        else if (driveMode() == DriveMode.RIGHT_ONLY)
            return calculateThrottlePair(-JoystickController.getY(rJoy), JoystickController.getTwist(rJoy)).first
        return 0.0
    }

    fun getRightDrive(): Double {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()

        if (driveMode() == DriveMode.BOTH)
            return -JoystickController.getY(rJoy)
        else if (driveMode() == DriveMode.LEFT_ONLY)
            return calculateThrottlePair(-JoystickController.getY(lJoy), JoystickController.getX(lJoy)).second
        else if (driveMode() == DriveMode.RIGHT_ONLY)
            return calculateThrottlePair(-JoystickController.getY(rJoy), JoystickController.getX(rJoy)).second
        return 0.0
    }

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

    internal fun limit(value: Double): Double {
        return Math.min(Math.max(value, -1.0), 1.0)
    }
}