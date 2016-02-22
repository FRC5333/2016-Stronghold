package frc.team5333.core.control.strategy

import frc.team5333.core.Core
import frc.team5333.core.control.ControlLease
import frc.team5333.core.systems.DriveSystem
import frc.team5333.core.systems.Systems

class StrategySpeedTest : Strategy() {

    var startTime: Long = 0
    var endTime: Long = 0

    lateinit var lease: ControlLease.Lease<DriveSystem>

    override fun getName(): String = "Speed Test"

    override fun onEnable() {
        super.onEnable()
        startTime = System.currentTimeMillis()
        endTime = startTime + 1000

        lease = Systems.drive.LEASE.acquire(ControlLease.Priority.HIGH)
    }

    override fun onDisable() {
        super.onDisable()
        lease.use { it.drive(0.0, 0.0) }
        lease.release()
    }

    override fun tick() {
        lease.use {
            var now = System.currentTimeMillis()
            it.drive(-1.0, -1.0)
        }
    }

    override fun isOperatorControl(): Boolean = false

    override fun isComplete(): Boolean = System.currentTimeMillis() >= endTime
}