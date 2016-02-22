package frc.team5333.core.vision

import frc.team5333.core.hardware.IO
import jaci.openrio.toast.lib.math.MathHelper

class VisionFrame {

    var x: Double = 0.0
    var y: Double = 0.0
    var width: Double = 0.0
    var height: Double = 0.0

    var field_heading: Double = 0.0
    var actualWidth = 0.0

    fun fin() {
        field_heading = MathHelper.d2r(IO.maybeIMU { it.angleY })
        actualWidth = width * Math.cos(field_heading)
    }

    fun area(): Double = width * height

    fun offsetCenterX(): Double {
        var centerRectX = x + (width / 2.0)
        var centerFrame = 640.0 / 2.0
        return (centerFrame - centerRectX) / centerFrame
    }

}