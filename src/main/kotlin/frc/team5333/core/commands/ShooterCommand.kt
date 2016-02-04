package frc.team5333.core.commands

import frc.team5333.core.Core
import frc.team5333.core.systems.ShooterSystem
import jaci.openrio.toast.core.command.AbstractCommand
import jaci.openrio.toast.core.command.IHelpable
import jaci.openrio.toast.lib.math.MathHelper
import kotlin.text.toDouble

class ShooterCommand : AbstractCommand(), IHelpable {
    override fun getHelp(): String? = "Shoot a Boulder. Usage: shoot <x> <y> [angle]"

    override fun getCommandName(): String? = "shoot"

    override fun invokeCommand(argLength: Int, args: Array<out String>, command: String?) {
        var x = args[0].toDouble()
        var y = args[1].toDouble()
        var angle = 45.0

        if (argLength > 2) angle = args[2].toDouble()

        angle = MathHelper.d2r(angle)

        var traj = ShooterSystem.ShooterTrajectory(x, y, angle)
        traj.shoot()

        Core.logger.info("Kaboom!")
    }

}