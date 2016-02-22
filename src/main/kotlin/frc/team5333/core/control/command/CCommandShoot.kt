package frc.team5333.core.control.command

import edu.wpi.first.wpilibj.command.Command
import frc.team5333.core.Core
import frc.team5333.core.control.ControlLease
import frc.team5333.core.systems.ShooterSystem
import frc.team5333.core.systems.Systems

class CCommandShoot(var top: Double, var bottom: Double) : Command() {

    val SPINUP_PERIOD = 2000

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

            if (elapsed < SPINUP_PERIOD) {
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
        return elapsed > SPINUP_PERIOD + 1000
    }
}