package frc.team5333.core

import frc.team5333.core.operator.Operator

class ControlManager {

    enum class DriveMode {
        LEFT_ONLY, RIGHT_ONLY, BOTH, NEITHER
    }

    fun driveMode(): DriveMode {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()

        if (lJoy.getTrigger() && rJoy.getTrigger())
            return DriveMode.BOTH
        else if (lJoy.getTrigger())
            return DriveMode.LEFT_ONLY
        else if (rJoy.getTrigger())
            return DriveMode.RIGHT_ONLY
        else
            return DriveMode.NEITHER
    }

//    fun leftDrive(): Double {
//
//    }
//
//    fun rightDrive(): Double {
//
//    }

}