package frc.team5333.core.control.strategy

import frc.team5333.core.control.ControlLease
import frc.team5333.core.systems.ShooterSystem
import frc.team5333.core.systems.Systems

class StrategySpinup(var top: Double, var bottom: Double, var intake: Double, var slave: Strategy) : Strategy() {

    lateinit var lease: ControlLease.Lease<ShooterSystem>

    override fun getName(): String = "Spinup[${slave.getName()}]"

    override fun tick() { }

    override fun tickFast() {
        lease.use {
            it.setTop(-top)
            it.setBottom(-bottom)
            it.setIntake(intake)
        }
        if (slave.isFast()) slave.tickFast() else slave.tick()
    }

    override fun isOperatorControl(): Boolean = slave.isOperatorControl()

    override fun isComplete(): Boolean = slave.isComplete()

    override fun onEnable() {
        super.onEnable()
        slave.onEnable()
        lease = Systems.shooter.LEASE.acquire(ControlLease.Priority.HIGHER)
    }

    override fun onDisable() {
        super.onDisable()
        slave.onDisable()
        lease.release()
    }

    override fun isFast(): Boolean  = true
}