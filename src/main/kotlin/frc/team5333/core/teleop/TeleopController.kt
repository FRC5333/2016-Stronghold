package frc.team5333.core.teleop

import frc.team5333.core.events.StrategyEvent
import frc.team5333.lib.events.EventBus
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
        EventBus.INSTANCE.raiseEvent(StrategyEvent.StrategyChangeEvent(value, activeStrategy))
        value.onEnable()
    }

    /**
     * Tick the active strategy periodically
     */
    fun tick() {
        activeStrategy.tick()
    }

}