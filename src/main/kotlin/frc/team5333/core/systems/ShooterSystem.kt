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

    var boulder_frontal_area = 2 * Math.PI * 0.254 * 0.254
    var boulder_mass = 0.1
    var drag_co_coefficient = 0.5 * 1.225 * 0.47 * boulder_frontal_area

    fun calculate_force_drag(velocity: Double): Double = drag_co_coefficient * velocity * velocity
    fun acceleration_from_force(force: Double): Double = force / boulder_mass



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
//        var vel = ShooterSystem.INSTANCE.calculate_launch_velocity(x_distance, y_distance, launch_angle)
//        var time = ShooterSystem.INSTANCE.calculate_time(x_distance, y_distance, launch_angle)
//        var range = 0.0..x_distance
//
//        fun it(step: Double) = Range(0.0, x_distance, step)
//        fun itTime(step: Double) = Range(0.0, time, step)
//        fun y_at(x: Double): Double = INSTANCE.calculate_y_at_x(vel, launch_angle, x)
//
//        fun shoot() = EventBus.INSTANCE.raiseEvent(ShootEvent(this))
    }

}