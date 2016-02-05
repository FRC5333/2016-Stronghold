package frc.team5333.core.systems

import frc.team5333.core.events.ShootEvent
import frc.team5333.lib.events.EventBus
import java.util.function.Consumer
import frc.team5333.lib.util.Range
import kotlin.ranges.rangeTo

enum class ShooterSystem {
    INSTANCE;

    var gravity = 9.81
    var flywheel_diameter = 0.15
    var flywheel_count = 1

    fun calculate_launch_velocity(x_distance: Double, y_distance: Double, launchAngle: Double): Double {
        var u_x = calculate_launch_velocity_x(x_distance, y_distance, launchAngle)
        return u_x / Math.cos(launchAngle)
    }

    fun calculate_launch_velocity_x(x_distance: Double, y_distance: Double, launchAngle: Double): Double {
        var time = calculate_time(x_distance, y_distance, launchAngle)
        return x_distance / time
    }

    fun calculate_time(x_distance: Double, y_distance: Double, launchAngle: Double): Double {
        return Math.sqrt( (x_distance * Math.tan(launchAngle) - y_distance) / (0.5 * gravity) )
    }

    fun calculate_position_at_time(launchVelocity: Double, launchAngle: Double, time: Double): Pair<Double, Double> {
        var x = launchVelocity * Math.cos(launchAngle) * time
        var y = launchVelocity * Math.sin(launchAngle) * time + 0.5 * -gravity * time * time
        return Pair(x, y)
    }

    fun calculate_y_at_x(launchVelocity: Double, launchAngle: Double, x: Double): Double {
        var time = x / (launchVelocity * Math.cos(launchAngle))
        var y = calculate_position_at_time(launchVelocity, launchAngle, time).second
        return y
    }

    fun calculate_rpm(launchVelocity: Double): Double = launchVelocity / ((flywheel_diameter / 2) * 1.0472 * flywheel_count)

    // TODO x velocity and y velocity based on drag

    class ShooterTrajectory(var x_distance: Double, var y_distance: Double, var launch_angle: Double) {
        var vel = ShooterSystem.INSTANCE.calculate_launch_velocity(x_distance, y_distance, launch_angle)
        var range = 0.0..x_distance

        fun it(step: Double) = Range(0.0, x_distance, step)
        fun y_at(x: Double): Double = INSTANCE.calculate_y_at_x(vel, launch_angle, x)

        fun shoot() = EventBus.INSTANCE.raiseEvent(ShootEvent(this))
    }

}