package frc.team5333.core.commands

import frc.team5333.core.control.strategy.StrategyController
import frc.team5333.core.control.strategy.StrategyOperator
import frc.team5333.core.control.strategy.StrategySpeedTest
import jaci.openrio.toast.core.command.AbstractCommand
import jaci.openrio.toast.core.command.IHelpable

class CommandSpeedTest : AbstractCommand(), IHelpable {

    override fun getCommandName(): String? = "speedtest"

    override fun invokeCommand(argLength: Int, args: Array<out String>?, command: String?) {
        var strat = StrategySpeedTest()
        strat.then(StrategyOperator())
        StrategyController.INSTANCE.setStrategy(strat)
    }

    override fun getHelp(): String? = "Conduct a Speed Test for 1 Second. Measuring the distance the Robot has covered on the field will give you the speed of the Robot in m/s"

}