package frc.team5333.core.systems

import frc.team5333.core.control.Operator

class ControlSystem {

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

}