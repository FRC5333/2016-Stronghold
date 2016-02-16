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

        IO.motor_flywheel_top.set(ControlManager.INSTANCE.getFlywheelTop())
        IO.motor_flywheel_bottom.set(ControlManager.INSTANCE.getFlywheelBottom())
    }
}