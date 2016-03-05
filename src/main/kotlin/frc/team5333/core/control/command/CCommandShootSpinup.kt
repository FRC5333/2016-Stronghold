package frc.team5333.core.control.command

import edu.wpi.first.wpilibj.command.Command
import frc.team5333.core.control.ControlLease
import frc.team5333.core.systems.ShooterSystem
import frc.team5333.core.systems.Systems

class CCommandShootSpinup(var top: Double, var bottom: Double, var spinup_period: Double = 2000.0) : Command() {

    var startTime = 0L
    lateinit var lease: ControlLease.Lease<ShooterSystem>

    override fun initialize() {
        startTime = System.currentTimeMillis()
        lease = Systems.shooter.LEASE.acquire(ControlLease.Priority.HIGHEST)
    }

    override fun execute() {
        var elapsed = System.currentTimeMillis() - startTime
        lease.use {
            it.setTop(-top)
            it.setBottom(-bottom)

            if (elapsed < spinup_period) {
                it.setIntake(-0.5)
            } else {
                it.setIntake(1.0)
            }
        }
    }

    override fun end() {
        lease.use { it.setAll(0.0) }
        lease.release()
    }

    override fun interrupted() {
        lease.use { it.setAll(0.0) }
        lease.release()
    }

    override fun isFinished(): Boolean {
        var elapsed = System.currentTimeMillis() - startTime
        return elapsed > spinup_period + (spinup_period / 2)
    }
}