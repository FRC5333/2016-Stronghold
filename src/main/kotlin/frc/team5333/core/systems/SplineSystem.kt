package frc.team5333.core.systems

import frc.team5333.core.Core
import frc.team5333.core.network.NetworkHub
import kotlin.collections.forEach
import kotlin.collections.map

enum class SplineSystem {
    INSTANCE;

    class Waypoint(var x:Double, var y:Double, var angle:Double)
    class Segment(var x:Double, var y:Double, var position:Double, var velocity:Double, var acceleration:Double, var jerk:Double, var heading:Double);
    class Spline(segments: List<Segment>)

    fun generateCentralSpline(vararg points: Waypoint): Spline {
        writeToCoprocessor(Core.config.getFloat("motion.max_velocity", 10.0f),
                Core.config.getFloat("motion.max_acceleration", 15.0f), points)
        return readFromCoprocessor()
    }

    fun writeToCoprocessor(max_velocity: Float, max_acceleration: Float, points: Array<out Waypoint>) {
        var hub = NetworkHub.INSTANCE
        var sock = NetworkHub.PROCESSORS.SPLINES.active!!
        var out = sock.outputStream

        out.write(hub.intToBytes(0xAB))     // Negotiation Byte
        out.write(hub.floatToBytes(max_velocity))
        out.write(hub.floatToBytes(max_acceleration))
        out.write(hub.intToBytes(points.size))

        points.forEach {
            out.write(hub.floatToBytes(it.x.toFloat()))
            out.write(hub.floatToBytes(it.y.toFloat()))
            out.write(hub.floatToBytes(it.angle.toFloat()))
        }
    }

    fun readFromCoprocessor(): Spline {
        var hub = NetworkHub.INSTANCE
        var sock = NetworkHub.PROCESSORS.SPLINES.active!!
        var inp = sock.inputStream

        while (true) {
            if (hub.readInt(inp) == 0xBA) {
                // Negotiation Byte
                var len = hub.readInt(inp)

                var segments = arrayOfNulls<Segment>(len)

                for (i in 0..len - 1) {
                    var x = hub.readFloat(inp).toDouble()
                    var y = hub.readFloat(inp).toDouble()
                    var pos = hub.readFloat(inp).toDouble()
                    var vel = hub.readFloat(inp).toDouble()
                    var acc = hub.readFloat(inp).toDouble()
                    var jerk = hub.readFloat(inp).toDouble()
                    var head = hub.readFloat(inp).toDouble()

                    segments.set(i, Segment(x, y, pos, vel, acc, jerk, head))
                }

                return Spline(segments.map { it!! })
            }
        }
    }

}