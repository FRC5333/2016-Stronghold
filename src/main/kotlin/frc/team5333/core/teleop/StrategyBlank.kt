package frc.team5333.core.teleop

import frc.team5333.core.hardware.IO

/**
 * A Blank Teleop Strategy, that sets all motors to 0.0. This is used to completely disable control by Driver or Auto.
 *
 * @author Jaci
 */
class StrategyBlank : TeleopStrategy {

    override fun getName(): String = "[ BLANK ]"

    override fun isOperatorControl() = false

    override fun tick() {
        IO.all_motors.set(0.0)
    }

}