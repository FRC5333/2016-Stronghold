package frc.team5333.core.teleop

import frc.team5333.core.hardware.IO

class StrategyBlank : TeleopStrategy {

    override fun getName(): String = "[NONE]"

    override fun tick() {
        IO.all_motors.set(0.0)
    }

}