package frc.team5333.core.systems

import edu.wpi.first.wpilibj.Joystick
import frc.team5333.core.control.Operator
import java.util.*

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

    fun tick() {  }

    fun driveJoystick(): Optional<Joystick> {
        var dm = driveMode()
        if (dm == DriveMode.LEFT_ONLY) return Optional.of(Operator.getLeftJoystick())
        if (dm == DriveMode.RIGHT_ONLY) return Optional.of(Operator.getRightJoystick())
        return Optional.empty()
    }

    fun shootJoystick(): Optional<Joystick> {
        var dm = driveMode()
        if (dm == DriveMode.LEFT_ONLY) return Optional.of(Operator.getRightJoystick())
        if (dm == DriveMode.RIGHT_ONLY) return Optional.of(Operator.getLeftJoystick())
        return Optional.empty()
    }

    fun driveButton(buttonID: Int): Boolean {
        var j = driveJoystick()
        if (j.isPresent) return j.get().getRawButton(buttonID)
        return false
    }

    fun shootButton(buttonID: Int): Boolean {
        var j = shootJoystick()
        if (j.isPresent) return j.get().getRawButton(buttonID)
        return false
    }

}