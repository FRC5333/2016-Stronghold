package frc.team5333.core.vision

import frc.team5333.core.hardware.IO
import jaci.openrio.toast.lib.math.MathHelper

class VisionFrame {

    var x: Double = 0.0
    var y: Double = 0.0
    var width: Double = 0.0
    var height: Double = 0.0

    var depth_mm: Double = 0.0
    var depth_local_mm: Double = 0.0

    var field_heading: Double = 0.0

    fun fin() {
        field_heading = MathHelper.d2r(IO.maybeIMU { it.angleY })
    }

    fun posRelTarget(): Pair<Double, Double> {
        return Pair(depth_local_mm * Math.sin(field_heading), depth_local_mm * Math.cos(field_heading))
    }

    fun area(): Double = width * height

}