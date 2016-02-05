package frc.team5333.core.events

import frc.team5333.core.teleop.TeleopStrategy
import frc.team5333.lib.events.EventBase

open class StrategyEvent : EventBase() {

    class StrategyChangeEvent(var newStrategy: TeleopStrategy, var oldStrategy: TeleopStrategy) : StrategyEvent() { }

}