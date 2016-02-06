package frc.team5333.core.systems

import frc.team5333.core.events.ShootEvent
import frc.team5333.lib.events.EventBus
import java.util.function.Consumer
import frc.team5333.lib.util.Range
import kotlin.ranges.rangeTo

/**
 * The ShooterSystem is the subsystem for the Shooting Mechanism. Our Shooting Mechanism is fixed at a 45 degree angle
 * with a variable launch velocity. This class's main purpose is to calculate the Launch Velocity based on our distance
 * to the target and our height plane delta to the target.
 *
 * To listen for the Shooting of a Boulder, subscribe to the ShootEvent.
 *
 * @author Jaci
 */
enum class ShooterSystem {
    INSTANCE;

    /**
     * Acceleration due to Gravity in Meters per Second per Second
     */
    var gravity = 9.81

    /**
     * Diameter of the flywheels in Meters
     */
    var flywheel_diameter = 0.15

    /**
     * The number of flywheels touching the Boulder at the time of launch
     */
    var flywheel_count = 1

    /**
     * Calculate the Launch Velocity (in Meters per Second) for the Boulder based on the X distance to the Target
     * (straight line distance, in meters), the Y delta between the plane of shooting and plane of landing (in meters),
     * and the Launch Angle (exit angle of the Boulder out of the mechanism, in radians)
     */
    fun calculate_launch_velocity(x_distance: Double, y_distance: Double, launchAngle: Double): Double {
        var u_x = calculate_launch_velocity_x(x_distance, y_distance, launchAngle)
        return u_x / Math.cos(launchAngle)
    }

    /**
     * Calculate the X Component of the Launch Velocity (in Meters per Second) for the Boulder based on the X distance to the Target
     * (straight line distance, in meters), the Y delta between the plane of shooting and plane of landing (in meters),
     * and the Launch Angle (exit angle of the Boulder out of the mechanism, in radians)
     */
    fun calculate_launch_velocity_x(x_distance: Double, y_distance: Double, launchAngle: Double): Double {
        var time = calculate_time(x_distance, y_distance, launchAngle)
        return x_distance / time
    }

    /**
     * Calculate the Time of Flight (in seconds) of the Boulder based on the X distance to the Target
     * (straight line distance, in meters), the Y delta between the plane of shooting and plane of landing (in meters),
     * and the Launch Angle (exit angle of the Boulder out of the mechanism, in radians)
     */
    fun calculate_time(x_distance: Double, y_distance: Double, launchAngle: Double): Double {
        return Math.sqrt( (x_distance * Math.tan(launchAngle) - y_distance) / (0.5 * gravity) )
    }

    /**
     * Calculate the X and Y position (in Meters) at a specified time (in seconds) of the Boulder, given the Launch
     * Velocity of the Boulder (in meters per second), and the Launch Angle (exit angle of the Boulder, in radians)
     */
    fun calculate_position_at_time(launchVelocity: Double, launchAngle: Double, time: Double): Pair<Double, Double> {
        var x = launchVelocity * Math.cos(launchAngle) * time
        var y = launchVelocity * Math.sin(launchAngle) * time + 0.5 * -gravity * time * time
        return Pair(x, y)
    }

    /**
     * Calculate the Y position of the Boulder (in meters) given the X position of the Boulder (in meters), Launch
     * Velocity of the Boulder (in meters per second), and the Launch Angle (exit angle of the Boulder, in radians)
     */
    fun calculate_y_at_x(launchVelocity: Double, launchAngle: Double, x: Double): Double {
        var time = x / (launchVelocity * Math.cos(launchAngle))
        var y = calculate_position_at_time(launchVelocity, launchAngle, time).second
        return y
    }

    /**
     * Calculate the RPM of each Flywheel in the Shooting Mechanism, given the desired Launch Velocity in Meters per Second
     */
    fun calculate_rpm(launchVelocity: Double): Double = launchVelocity / ((flywheel_diameter / 2) * 1.0472 * flywheel_count)

    // TODO x velocity and y velocity based on drag

    /**
     * The ShooterTrajectory is a container class for the path that a boulder is desired to follow. This includes the
     * X and Y distance, Launch Angle, Launch Velocity and Range.
     */
    class ShooterTrajectory(var x_distance: Double, var y_distance: Double, var launch_angle: Double) {
        var vel = ShooterSystem.INSTANCE.calculate_launch_velocity(x_distance, y_distance, launch_angle)
        var range = 0.0..x_distance

        fun it(step: Double) = Range(0.0, x_distance, step)
        fun y_at(x: Double): Double = INSTANCE.calculate_y_at_x(vel, launch_angle, x)

        fun shoot() = EventBus.INSTANCE.raiseEvent(ShootEvent(this))
    }

}