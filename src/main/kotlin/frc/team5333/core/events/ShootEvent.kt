package frc.team5333.core.events

import frc.team5333.core.systems.ShooterSystem
import frc.team5333.lib.events.EventBase

/**
 * The ShootEvent is called when a Boulder is fired. The Boulder is fired at priority NORMAL, meaning anything higher
 * than NORMAL will trigger before the Boulder is fired. The Boulder's firing may be cancelled by cancelling this
 * event before the NORMAL priority is reached
 *
 * @author Jaci
 */
class ShootEvent(var trajectory: ShooterSystem.ShooterTrajectory) : EventBase() { }