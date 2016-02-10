package frc.team5333.core.events

import frc.team5333.lib.events.EventBase
import jaci.openrio.toast.lib.state.RobotState

class StateChangeEvent(var fromState: RobotState, var toState: RobotState?) : EventBase() {

    override fun getCustomData(): String? {
        if (toState == null) return ""
        return toState!!.state
    }

}