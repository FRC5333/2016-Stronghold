package frc.team5333.core.teleop

import jaci.openrio.toast.lib.state.RobotState

public interface TeleopStrategy {

    fun getName(): String

    fun onEnable() { }

    fun onDisable() { }

    fun onSwitchedTo(lastState: RobotState) { }

    fun tick()

}