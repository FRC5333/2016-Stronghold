package frc.team5333.core.control.strategy

import frc.team5333.core.events.StrategyEvent
import frc.team5333.lib.events.EventBus

enum class StrategyController {
    INSTANCE;

    private var fast = false

    private var activeStrategy: Strategy = StrategyOperator()

    val lock = Object()

    /**
     * Tick the active strategy periodically
     */
    fun tick(fast: Boolean) {
        synchronized(lock) {
            if (activeStrategy.isComplete()) {
                var next_strat = activeStrategy.nextStrategy()
                if (next_strat != null) {
                    setStrategy_Now(next_strat)
                } else {
                    setStrategy_Now(StrategyOperator())
                }
            }
            if (fast)   activeStrategy.tickFast()
            else        activeStrategy.tick()
        }
    }

    fun tickFast() {
        if (fast) tick(true)
    }

    fun tickSlow() {
        tick(false)
    }

    fun setStrategy(strat: Strategy) {
        synchronized(lock) {
            setStrategy_Now(strat)
        }
    }

    fun getStrategy(): Strategy = activeStrategy

    private fun setStrategy_Now(value: Strategy) {
        activeStrategy.onDisable()
        EventBus.INSTANCE.raiseEvent(StrategyEvent.StrategyChangeEvent(value, activeStrategy))
        fast = value.isFast()
        activeStrategy = value
        value.onEnable()
    }

}