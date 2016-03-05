package frc.team5333.core.control

class InternalPID(var Kp: Double, var Ki: Double, var Kd: Double) {

    var target = 0.0

    private var last_time = 0L
    var last_error = 0.0

    private var firstrun = true

    private var running_i = 0.0

    fun update(feedback: Double): Double {
        var error = target - feedback
        var time = System.currentTimeMillis()

        if (firstrun) {
            last_time = time
            firstrun = false
        }

        var dt = time - last_time
        var de = error - last_error

        var p = Kp * error
        running_i += error * dt
        var d = 0.0
        if (dt > 0.0) d = de / dt

        last_time = time
        last_error = error

        return p + running_i * Ki + d * Kd
    }

}