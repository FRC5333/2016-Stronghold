package frc.team5333.core.control.profiling

import edu.wpi.first.wpilibj.CANTalon
import frc.team5333.core.Core
import frc.team5333.core.network.NetworkHub
import kotlin.collections.*

/**
 * Spline and Motion Profiling generation subsystem. All values are in Metric Units unless otherwise stated. Not your
 * silly 'Freedom Units' like feet, inches and dick size.
 *
 * Motion Profiling is a way for the Robot to accurately move from point a to point b in a smooth, nice motion using
 * spline fitting. This means our robot cha chas real smooth
 *
 * @author Jaci
 */
enum class SplineSystem {
    INSTANCE;

    class Waypoint(var x:Double, var y:Double, var angle:Double)
    class Segment(var x:Double, var y:Double, var position:Double, var velocity:Double, var acceleration:Double, var jerk:Double, var heading:Double) {
        constructor(seg: Segment) : this(seg.x, seg.y, seg.position, seg.velocity, seg.acceleration, seg.jerk, seg.heading)
    }

    class Trajectory(var segments: Array<Segment>) {
        fun copy(): Trajectory {
            var newTraj = Trajectory(segments)
            newTraj.segments = newTraj.segments.map { Segment(it) }.toTypedArray()
            return newTraj
        }

        fun getLength(): Int = segments.size
        fun get(i: Int): Segment = segments.get(i)
        fun set(i: Int, seg: Segment) = segments.set(i, seg)
    }

    fun generateTrajectoryPairs(points: Array<out Waypoint>): Pair<Trajectory, Trajectory> {
        return createTrajectoryPair(generateCentralTrajectory(points), Core.config.getDouble("motion.wheelbase_width", 0.633294))
    }

    fun generateCentralTrajectory(points: Array<out Waypoint>): Trajectory {
        writeToCoprocessor(Core.config.getFloat("motion.max_velocity", 1.5f),
                Core.config.getFloat("motion.max_acceleration", 7.0f), points)
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

    fun readFromCoprocessor(): Trajectory {
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

                return Trajectory(segments.map { it!! }.toTypedArray())
            }
        }
    }

    fun createTrajectoryPair(original: Trajectory, width: Double): Pair<Trajectory, Trajectory> {
        var out = Pair(original.copy(), original.copy())
        var w = width / 2

        for (i in 0..original.getLength() - 1) {
            var seg = original.get(i)
            var ca = Math.cos(seg.heading)
            var sa = Math.sin(seg.heading)

            var seg1 = out.first.get(i)
            seg1.x = seg.x - (w * sa)
            seg1.y = seg.y + (w * ca)

            if (i > 0) {
                var last = out.first.get(i - 1)
                var distance = Math.sqrt((seg1.x - last.x) * (seg1.x - last.x) + (seg1.y - last.y) * (seg1.y - last.y))
                seg1.position = last.position + distance
                seg1.velocity = distance / 0.05
                seg1.acceleration = (seg1.velocity - last.velocity) / 0.05
                seg1.jerk = (seg1.acceleration - last.acceleration) / 0.05
            }

            var seg2 = out.second.get(i)
            seg2.x = seg.x + (w * sa)
            seg2.y = seg.y - (w * ca)

            if (i > 0) {
                var last = out.second.get(i - 1)
                var distance = Math.sqrt((seg2.x - last.x) * (seg2.x - last.x) + (seg2.y - last.y) * (seg2.y - last.y))
                seg2.position = last.position + distance
                seg2.velocity = distance / 0.05
                seg2.acceleration = (seg2.velocity - last.velocity) / 0.05
                seg2.jerk = (seg2.acceleration - last.acceleration) / 0.05
            }
        }
        return out
    }

}