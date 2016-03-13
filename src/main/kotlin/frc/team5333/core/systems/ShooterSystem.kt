package frc.team5333.core.systems

import edu.wpi.first.wpilibj.CANTalon
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.Talon
import frc.team5333.core.control.ControlLease
import frc.team5333.core.control.Joy
import frc.team5333.core.control.Operator
import frc.team5333.core.vision.VisionNetwork

class ShooterSystem(var flywheelTop: CANTalon, var flywheelBottom: CANTalon, var intake: Talon) {
    val LEASE = ControlLease(this)
    var LOOKUP = ShooterLookup()

    var passiveSpinup = false
    lateinit private var spinup_lease: ControlLease.Lease<ShooterSystem>

    // AUTOMATIC CONTROL

    fun init() {
        LOOKUP.loadFromFile()
        spinup_lease = LEASE.acquire(ControlLease.Priority.HIGH)
    }

    fun sync() {
        LOOKUP.saveToFile()
    }

    fun tick() {
        if (passiveSpinup) {
            // Standard "Leap Of Faith" values. A spin-down is free, but a spin-up takes energy, hence this is higher than the 50 percentile
            var top = 0.8
            var btm = 0.8

            // If we have a vision frame, use that to get our spin values
            if (VisionNetwork.INSTANCE.active != null) {
                var p = Systems.shooter.LOOKUP.get(VisionNetwork.INSTANCE.active.y)
                top = p.first
                btm = p.second
            }

            spinup_lease.use {
                it.setTop(top)
                it.setBottom(btm)
                it.setIntake(-0.25)      // Hold Boulder
            }
        }
    }

    // MANUAL CONTROL

    fun setTop(v: Double) = flywheelTop.set(v)
    fun setBottom(v: Double) = flywheelBottom.set(v)
    fun setIntake(v: Double) = intake.set(v)
    fun setAll(v: Double) {
        setTop(v)
        setBottom(v)
        setIntake(v)
    }

    fun runFlywheels() {
        var pairs = getFlywheelPairs()
        flywheelTop.set(pairs.first)
        flywheelBottom.set(pairs.first)
        intake.set(pairs.second)
    }

    fun getFlywheelPairs(): Pair<Double, Double> {
        var lJoy = Operator.getLeftJoystick()
        var rJoy = Operator.getRightJoystick()
        var mode = Systems.control.driveMode()

        if (mode == ControlSystem.DriveMode.LEFT_ONLY) {
            return calc(rJoy);
        } else if (mode == ControlSystem.DriveMode.RIGHT_ONLY) {
            return calc(lJoy);
        } else {
            return Pair(0.0, 0.0)
        }
    }

    internal fun calc(joy: Joystick): Pair<Double, Double> {
        return Pair(sq(joy.getY()), sq(Joy.getX(joy)))
    }

    internal fun sq(value: Double): Double {
        if (value >= 0.0)
            return value * value
        else
            return -(value * value)
    }

}