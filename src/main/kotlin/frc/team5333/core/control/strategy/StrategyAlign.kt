package frc.team5333.core.control.strategy

import frc.team5333.core.Core
import frc.team5333.core.control.ControlLease
import frc.team5333.core.control.InternalPID
import frc.team5333.core.systems.DriveSystem
import frc.team5333.core.systems.Systems
import frc.team5333.core.vision.VisionNetwork


class StrategyAlign : Strategy() {
    override fun getName(): String = "Align"

    var kp = 0.0
    var ki = 0.0
    var kd = 0.0

    var started = false
    var start_time = 0L

    lateinit var internal_pid: InternalPID
    lateinit var lease: ControlLease.Lease<DriveSystem>

    override fun onEnable() {
        super.onEnable()
        kp = Core.config.getDouble("vision.align.p", 0.7)
        ki = Core.config.getDouble("vision.align.i", 0.001)
        kd = Core.config.getDouble("vision.align.d", 0.05)
        lease = Systems.drive.LEASE.acquire(ControlLease.Priority.HIGH)
        internal_pid = InternalPID(kp, ki, kd)
        internal_pid.target = 0.0
    }

    override fun tick() { }

    override fun tickFast() {
        if (!started) start_time = System.currentTimeMillis()
        started = true
        if (VisionNetwork.INSTANCE.active != null) {
            var error = VisionNetwork.INSTANCE.active.offsetCenterX()
            var turnV = internal_pid.update(error)

            lease.use {
                it.drive(turnV, -turnV)
            }
        } else {
            internal_pid.last_error = 0.0
            Core.logger.info("Could not align: No Frame!")
        }
    }

    override fun isOperatorControl(): Boolean = false

    override fun isFast(): Boolean = true

    override fun isComplete(): Boolean = started && (Math.abs(internal_pid.last_error) <= 0.05 || System.currentTimeMillis() - start_time > 2000)   // 2 sec align time
}