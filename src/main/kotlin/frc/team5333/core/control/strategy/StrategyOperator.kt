package frc.team5333.core.control.strategy

import frc.team5333.core.control.ControlManager
import frc.team5333.core.hardware.IO

class StrategyOperator : Strategy() {
    override fun getName(): String = "Operator"

    override fun isOperatorControl(): Boolean = true

    override fun tick() {
        ControlManager.INSTANCE.update()
        IO.setLeftMotors(ControlManager.INSTANCE.getLeftDrive())
        IO.setRightMotors(-ControlManager.INSTANCE.getRightDrive())
    }
}