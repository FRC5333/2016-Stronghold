package frc.team5333.core.events

import frc.team5333.core.data.DefenseInfo
import frc.team5333.lib.events.EventBase

class DefenseChangedEvent(var defenses : Array<out DefenseInfo.Defense>) : EventBase()
class PlacementChangedEvent() : EventBase()