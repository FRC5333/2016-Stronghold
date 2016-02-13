package frc.team5333.core.systems

import kotlin.concurrent.currentThread

class SplineFollower {

    var kp = 0.0
    var ki = 0.0
    var kd = 0.0
    var kv = 0.0
    var ka = 0.0

    var last_error = 0.0
    var heading = 0.0

    var segment = 0
    var trajectory: SplineSystem.Trajectory? = null

    fun configurePID_VA(kkp:Double, kki:Double, kkd:Double, kkv:Double, kka:Double) {
        kp = kkp; ki = kki; kd = kkd; kv = kkv; ka = kka
    }

    fun reset() {
        last_error = 0.0
        segment = 0
    }

    fun calculate(distance: Double): Double {
        var traj = trajectory!!
        if (segment < traj.getLength()) {
            var seg = traj.get(segment)
            var error = seg.position - distance
            var calculated_value =
                    kp * error +
                    kd * ((error - last_error) / 0.01 - seg.velocity) +
                    (kv * seg.velocity + ka * seg.acceleration)
            last_error = error
            heading = seg.heading
            segment++
            return calculated_value
        }
        return 0.0
    }

    fun finished():Boolean = segment >= trajectory!!.getLength()

}