package frc.team5333.core.teleop

import frc.team5333.core.control.ControlManager
import frc.team5333.core.hardware.IO

class StrategyOperator : TeleopStrategy {

    override fun getName(): String = "Operator"

    override fun tick() {
        IO.setLeftMotors(ControlManager.INSTANCE.getLeftDrive())
        IO.setRightMotors(ControlManager.INSTANCE.getRightDrive())
    }
}