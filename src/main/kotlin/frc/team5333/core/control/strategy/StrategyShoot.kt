package frc.team5333.core.control.strategy

import frc.team5333.core.Core
import frc.team5333.core.control.command.CCommandShootSpinup
import frc.team5333.core.systems.Systems
import frc.team5333.core.vision.VisionNetwork

class StrategyShoot(var top: Double, var btm: Double, var spun: Boolean = false) : Strategy() {

    constructor(y: Double) : this(0.0, 0.0) {
        var p = Systems.shooter.LOOKUP.get(y)
        this.top = p.first
        this.btm = p.second
    }

    var genOnEnable = false
    var instant = false

    constructor() : this(0.0, 0.0) {
        genOnEnable = true
    }

    lateinit var command: CCommandShootSpinup

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
                Core.logger.warn("Couldn't acquire vision target for shooting: Taking a shot of faith!")
            }
        }

        if (instant) {
            command = CCommandShootSpinup(top, btm)
            command.instant = true
        } else if (spun)
            command = CCommandShootSpinup(top, btm)
        else
            command = CCommandShootSpinup(top, btm, 2000.0)
        command.start()
    }

    override fun tick() { }

    override fun isOperatorControl(): Boolean = false

    override fun isComplete(): Boolean = !command.isRunning
}