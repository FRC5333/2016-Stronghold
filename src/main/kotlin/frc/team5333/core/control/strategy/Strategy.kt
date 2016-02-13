package frc.team5333.core.control.strategy

abstract class Strategy {

    private var strat: Strategy? = null

    abstract fun getName(): String

    fun onEnable() { }

    fun onDisable() { }

    abstract fun tick()

    abstract fun isOperatorControl(): Boolean

    fun isComplete(): Boolean = false

    fun then(strategy: Strategy): Strategy {
        strat = strategy
        return strat!!
    }

    fun nextStrategy(): Strategy? {
        return strat
    }

    fun isFast(): Boolean = false

}