package frc.team5333.core.hardware

import edu.wpi.first.wpilibj.SpeedController
import kotlin.collections.forEach

class MultiMotor(vararg motors: SpeedController) : SpeedController {

    var _motors = motors
    var _inverted = false
    var _speed = 0.0

    override fun setInverted(isInverted: Boolean) {
        _motors.forEach { it.inverted = isInverted }
        _inverted = isInverted
    }

    override fun getInverted(): Boolean = _inverted

    override fun set(speed: Double, syncGroup: Byte) {
        _motors.forEach { it.set(speed, syncGroup) }
        _speed = speed
    }

    override fun set(speed: Double) {
        _motors.forEach { it.set(speed) }
        _speed = speed
    }

    override fun get(): Double = _speed

    override fun disable() {
        _motors.forEach { it.disable() }
        _speed = 0.0
    }

    override fun pidWrite(output: Double) {
        _motors.forEach { it.pidWrite(output) }
    }

}