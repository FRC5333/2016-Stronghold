package frc.team5333.core.teleop

import jaci.openrio.toast.lib.state.RobotState
import kotlin.reflect.KClass

enum class TeleopController {
    INSTANCE;

    var activeStrategy: TeleopStrategy = StrategyOperator()

    fun tick() {
        activeStrategy.tick()
    }

    fun switchTo(lastState: RobotState) {
        activeStrategy.onSwitchedTo(lastState)
    }
}