package frc.team5333.core.teleop

import jaci.openrio.toast.lib.state.RobotState

/**
 * The TeleopController is the class responsible for keeping track of the current TeleopStrategies being used.
 *
 * This controls what is currently in control, to make sure the drive train does not receive mixed signals.
 *
 * @author Jaci
 */
enum class TeleopController {
    INSTANCE;

    var activeStrategy: TeleopStrategy = StrategyOperator()
    set(value) {
        activeStrategy.onDisable()
        value.onEnable()
    }

    /**
     * Tick the active strategy periodically
     */
    fun tick() {
        activeStrategy.tick()
    }

}