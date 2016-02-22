package frc.team5333.core.control.strategy

import edu.wpi.first.wpilibj.command.Command

class StrategyWrapper(var command: Command) : Strategy() {

    override fun getName(): String = "Wrapper[${command.name}]"

    override fun tick() { }

    override fun isOperatorControl(): Boolean = false

    override fun onEnable() {
        super.onEnable()
        command.start()
    }

    override fun onDisable() {
        super.onDisable()
        command.cancel()
    }

    override fun isComplete(): Boolean = !command.isRunning

}