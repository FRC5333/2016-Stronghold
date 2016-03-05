package frc.team5333.core.control.strategy

import frc.team5333.core.Core
import frc.team5333.core.control.ControlLease
import frc.team5333.core.control.InternalPID
import frc.team5333.core.hardware.IO
import frc.team5333.core.systems.DriveSystem
import frc.team5333.core.systems.Systems
import frc.team5333.core.vision.VisionNetwork
import frc.team5333.lib.util.MathUtil


class StrategyHeading(var desired: Double) : Strategy() {
    override fun getName(): String = "Heading"

    init {
        desired = MathUtil.boundHalfDeg(desired)
    }

    var kp = 0.0
    var ki = 0.0
    var kd = 0.0

    var started = false
    var start_time = 0L

    lateinit var internal_pid: InternalPID
    lateinit var lease: ControlLease.Lease<DriveSystem>

    override fun onEnable() {
        super.onEnable()
        kp = Core.config.getDouble("vision.heading.p", 0.7)
        ki = Core.config.getDouble("vision.heading.i", 0.001)
        kd = Core.config.getDouble("vision.heading.d", 0.05)
        lease = Systems.drive.LEASE.acquire(ControlLease.Priority.HIGH)
        internal_pid = InternalPID(kp, ki, kd)
        internal_pid.target = desired
    }

    override fun tick() { }

    override fun tickFast() {
        if (!started) start_time = System.currentTimeMillis()
        started = true

        var imu_heading = MathUtil.boundHalfDeg(IO.maybeIMU { it.angleY })

        lease.use {
            var turnV = internal_pid.update(imu_heading) / 180.0
            it.drive(-turnV, turnV)
        }
    }

    override fun isOperatorControl(): Boolean = false

    override fun isFast(): Boolean = true

    override fun isComplete(): Boolean = started && (Math.abs(internal_pid.last_error) <= 0.5 || System.currentTimeMillis() - start_time > 2000)   // 2 sec align time
}