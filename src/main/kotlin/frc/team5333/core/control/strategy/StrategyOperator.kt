package frc.team5333.core.control.strategy

import edu.wpi.first.wpilibj.CANTalon
import frc.team5333.core.control.ControlLease
import frc.team5333.core.hardware.IO
import frc.team5333.core.systems.DriveSystem
import frc.team5333.core.systems.ShooterSystem
import frc.team5333.core.systems.Systems

class StrategyOperator : Strategy() {

    lateinit var lease_drive: ControlLease.Lease<DriveSystem>
    lateinit var lease_shoot: ControlLease.Lease<ShooterSystem>

    override fun getName(): String = "Operator"

    override fun isOperatorControl(): Boolean = true

    override fun tick() {
        lease_drive.use { it.drive() }
        lease_shoot.use { it.runFlywheels() }
    }

    override fun onEnable() {
        super.onEnable()
        lease_drive = Systems.drive.LEASE.acquire(ControlLease.Priority.NORMAL)
        lease_shoot = Systems.shooter.LEASE.acquire(ControlLease.Priority.NORMAL)
    }

    override fun onDisable() {
        super.onDisable()
        lease_drive.release()
        lease_shoot.release()
    }
}