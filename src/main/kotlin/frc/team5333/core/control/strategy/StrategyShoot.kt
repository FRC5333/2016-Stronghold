package frc.team5333.core.control.strategy

import frc.team5333.core.control.command.CCommandShoot
import frc.team5333.core.systems.Systems
import frc.team5333.core.vision.VisionNetwork

class StrategyShoot(var top: Double, var btm: Double) : Strategy() {

    constructor(y: Double) : this(0.0, 0.0) {
        var p = Systems.shooter.LOOKUP.get(y)
        this.top = p.first
        this.btm = p.second
    }

    var genOnEnable = false

    constructor() : this(0.0, 0.0) {
        genOnEnable = true
    }

    lateinit var command: CCommandShoot

    override fun getName(): String = "Shoot"

    override fun onEnable() {
        super.onEnable()
        if (genOnEnable) {
            if (VisionNetwork.INSTANCE.active != null) {
                var p = Systems.shooter.LOOKUP.get(VisionNetwork.INSTANCE.active.y)
                this.top = p.first
                this.btm = p.second
            } else {
                this.top = 0.8      // Take a shot of faith
                this.btm = 0.8
            }
        }
        command = CCommandShoot(top, btm)
        command.start()
    }

    override fun tick() { }

    override fun isOperatorControl(): Boolean = false

    override fun isComplete(): Boolean = !command.isRunning
}