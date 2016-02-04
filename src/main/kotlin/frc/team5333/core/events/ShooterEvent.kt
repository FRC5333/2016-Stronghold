package frc.team5333.core.events

import frc.team5333.core.systems.ShooterSystem
import frc.team5333.lib.events.EventBase

class ShooterEvent(var trajectory: ShooterSystem.ShooterTrajectory) : EventBase() { }