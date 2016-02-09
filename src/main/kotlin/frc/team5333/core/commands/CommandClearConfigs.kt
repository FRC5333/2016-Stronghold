package frc.team5333.core.commands

import jaci.openrio.toast.core.command.AbstractCommand
import jaci.openrio.toast.core.command.IHelpable
import jaci.openrio.toast.lib.module.ModuleConfig
import java.io.File
import kotlin.collections.forEach

class CommandClearConfigs : AbstractCommand(), IHelpable {
    override fun getCommandName(): String? = "clear_cfg"

    override fun getHelp(): String? = "Clear all configurations"

    override fun invokeCommand(argLength: Int, args: Array<out String>?, command: String?) {
        ModuleConfig.allConfigs.forEach {
            var field = it.javaClass.getDeclaredField("parent_file")
            field.isAccessible = true
            var fl = field.get(it) as File
            fl.delete()
            fl.createNewFile()
            it.reload()
        }
    }
}