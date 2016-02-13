package frc.team5333.core.control.strategy

import frc.team5333.core.events.StrategyEvent
import frc.team5333.lib.events.EventBus

enum class StrategyController {
    INSTANCE;

    private var fast = false

    var activeStrategy: Strategy = StrategyOperator()
        private set(value) {
            activeStrategy.onDisable()
            EventBus.INSTANCE.raiseEvent(StrategyEvent.StrategyChangeEvent(value, activeStrategy))
            fast = value.isFast()
            value.onEnable()
        }

    val lock = Object()

    /**
     * Tick the active strategy periodically
     */
    fun tick() {
        synchronized(lock) {
            if (activeStrategy.isComplete()) {
                var next_strat = activeStrategy.nextStrategy()
                if (next_strat != null) {
                    activeStrategy = next_strat
                } else {
                    activeStrategy = StrategyOperator()
                }
            }
            activeStrategy.tick()
        }
    }

    fun tickFast() {
        if (fast) tick()
    }

    fun tickSlow() {
        if (!fast) tick()
    }

    fun setStrategy(strat: Strategy) {
        synchronized(lock) {
            activeStrategy = strat
        }
    }

}