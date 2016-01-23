package frc.team5333.core.teleop

import jaci.openrio.toast.lib.state.RobotState

/**
 * A TeleopStrategy is a control mode that the Robot undergoes during the Teleoperated Period. A TeleopStrategy
 * can be used by the Operator, or by an Automated System, for example, targetting.
 *
 * @author Jaci
 */
public interface TeleopStrategy {

    /**
     * Get the name of the TeleopStrategy. This is used in the Driver Station for the driver to view.
     */
    fun getName(): String

    fun onEnable() { }

    fun onDisable() { }

    /**
     * Tick the Strategy periodically during the Teleoperated Period
     */
    fun tick()

    /**
     * Does the Operator (Driver) control this Strategy? If false, the Strategy must be autonomous.
     */
    fun isOperatorControl(): Boolean

}