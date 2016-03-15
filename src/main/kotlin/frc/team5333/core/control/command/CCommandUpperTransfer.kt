package frc.team5333.core.control.command

import edu.wpi.first.wpilibj.command.Command
import frc.team5333.core.control.ControlLease
import frc.team5333.core.systems.ShooterSystem
import frc.team5333.core.systems.Systems

class CCommandUpperTransfer() : Command() {

    var startTime = 0L
    lateinit var lease: ControlLease.Lease<ShooterSystem>
    val SPINUP = 500;
    val TRANSFER = 1000;

    override fun initialize() {
        startTime = System.currentTimeMillis()
        lease = Systems.shooter.LEASE.acquire(ControlLease.Priority.HIGHEST)
    }

    override fun execute() {
        var elapsed = System.currentTimeMillis() - startTime
        lease.use {
            if (elapsed < SPINUP) {
                // By spinning the bottom forward and the top forward, we can create a 'tumbling' effect
                // when the boulder is transferred. This means the bottom flywheel not only assists in the
                // initial transfer of the boulder, but also that the boulder will not exit the shooting
                // chamber due to the top flywheel running as an intake.
                it.setBottom(-0.5)
                it.setTop(0.6)      // Slightly faster, just in case

                it.setIntake(-0.2)
            } else if (elapsed < SPINUP + TRANSFER) {
                it.setBottom(-0.5)
                it.setTop(0.6)

                it.setIntake(1.0)       // Give the boulder the energy needed to do a transfer
            } else {
                it.setTop(0.0)
                it.setBottom(0.0)
                it.setIntake(0.0)
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
        return elapsed > SPINUP + TRANSFER
    }
}