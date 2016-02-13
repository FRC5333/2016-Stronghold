package frc.team5333.core.control.strategy

abstract class Strategy {

    private var strat: Strategy? = null

    abstract fun getName(): String

    open fun onEnable() { }

    open fun onDisable() { }

    abstract fun tick()

    open fun tickFast() { }

    abstract fun isOperatorControl(): Boolean

    open fun isComplete(): Boolean = false

    fun then(strategy: Strategy): Strategy {
        strat = strategy
        return strat!!
    }

    fun nextStrategy(): Strategy? {
        return strat
    }

    open fun isFast(): Boolean = false

}