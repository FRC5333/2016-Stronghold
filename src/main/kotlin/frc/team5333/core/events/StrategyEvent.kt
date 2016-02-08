package frc.team5333.core.events

import frc.team5333.core.teleop.TeleopStrategy
import frc.team5333.lib.events.EventBase

/**
 * A StrategyEvent is called upon a change to the current Teleop Strategy
 *
 * @author Jaci
 */
open class StrategyEvent : EventBase() {

    /**
     * The StrategyChangeEvent is called upon a change to the currently active Teleop Strategy. This is used to
     * detect whether the driver does or doesn't have control, and for when different actions are being executed.
     *
     * @author Jaci
     */
    class StrategyChangeEvent(var newStrategy: TeleopStrategy, var oldStrategy: TeleopStrategy) : StrategyEvent() { }

}