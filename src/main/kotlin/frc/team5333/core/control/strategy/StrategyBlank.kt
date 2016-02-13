package frc.team5333.core.control.strategy

import frc.team5333.core.hardware.IO

class StrategyBlank : Strategy() {

    override fun getName(): String = "[ BLANK ]"

    override fun isOperatorControl() = false

    override fun tick() {
        IO.all_motors.set(0.0)
    }

}