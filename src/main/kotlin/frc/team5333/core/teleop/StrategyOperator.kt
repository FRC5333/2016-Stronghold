package frc.team5333.core.teleop

import frc.team5333.core.control.ControlManager
import frc.team5333.core.hardware.IO

/**
 * The Operator Strategy is used to control the Drive Train by the Operator Joysticks. This strategy is used in the
 * Teleoperated Period of the game, unless the Robot is undergoing it's own control for automated aiming and path
 * traversal.
 *
 * @author Jaci
 */
class StrategyOperator : TeleopStrategy {
    override fun getName(): String = "Operator"

    override fun isOperatorControl(): Boolean = true

    override fun tick() {
        ControlManager.INSTANCE.update()
        IO.setLeftMotors(ControlManager.INSTANCE.getLeftDrive())
        IO.setRightMotors(-ControlManager.INSTANCE.getRightDrive())
    }
}