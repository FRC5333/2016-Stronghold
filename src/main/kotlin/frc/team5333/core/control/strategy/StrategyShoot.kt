package frc.team5333.core.control.strategy

import frc.team5333.core.control.command.CCommandShoot
import frc.team5333.core.systems.Systems

class StrategyShoot(var top: Double, var btm: Double) : Strategy() {

    constructor(dist: Double) : this(0.0, 0.0) {
        var p = Systems.shooter.LOOKUP.get(dist)
        this.top = p.first
        this.btm = p.second
    }

    lateinit var command: CCommandShoot

    override fun getName(): String = "Shoot"

    override fun onEnable() {
        super.onEnable()
        command = CCommandShoot(top, btm)
        command.start()
    }

    override fun tick() { }

    override fun isOperatorControl(): Boolean = false

    override fun isComplete(): Boolean = !command.isRunning
}