package frc.team5333.core.control.strategy

import frc.team5333.core.Core
import frc.team5333.core.control.ControlLease
import frc.team5333.core.systems.DriveSystem
import frc.team5333.core.systems.Systems
import frc.team5333.core.vision.VisionNetwork


class StrategyAlign : Strategy() {

    override fun getName(): String = "Align"

    var kp = 0.0
    var ki = 0.0
    var kd = 0.0

    var lastError = 0.0
    var cumulativeError = 0.0
    var lastTime = 0L
    var started = false

    lateinit var lease: ControlLease.Lease<DriveSystem>

    override fun onEnable() {
        super.onEnable()
        kp = Core.config.getDouble("vision.align.p", 0.7)
        ki = Core.config.getDouble("vision.align.i", 0.001)
        kd = Core.config.getDouble("vision.align.d", 0.05)
        lastTime = System.currentTimeMillis()
        lease = Systems.drive.LEASE.acquire(ControlLease.Priority.HIGH)
    }

    override fun tick() {
        started = true
        if (VisionNetwork.INSTANCE.active != null) {
            var now = System.currentTimeMillis()
            var dt = now - lastTime

            var error = VisionNetwork.INSTANCE.active.offsetCenterX()
            cumulativeError += (error * dt)
            var deriv = (error - lastError) / dt.toDouble()

            var turnV = (kp * error + kd * deriv)

            lease.use {
                it.drive(turnV, -turnV)
            }

            lastError = error
            lastTime = now
        } else {
            lastError = 0.0
            Core.logger.info("Could not align: No Frame!")
        }
    }

    override fun isOperatorControl(): Boolean = false

    override fun isComplete(): Boolean = started && (Math.abs(lastError) <= 0.05 || System.currentTimeMillis() - lastTime > 2000)   // 2 sec align time
}