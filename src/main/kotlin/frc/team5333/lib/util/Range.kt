package frc.team5333.lib.util

import java.util.function.Consumer

/**
 * The Range Class is used to create a Min/Max range, and a Step value to be used for iteration over the range of numbers.
 * This is a simple utility class for dealing with iteration in an object-oriented manner.
 *
 * @author Jaci
 */
class Range(val min: Double, val max: Double, val step: Double) {

    infix fun forEach(action: Consumer<Double>) {
        var x = min
        while (x < max) {
            action.accept(x)
            x += step
        }
    }

    infix fun forEachInclusive(action: Consumer<Double>) {
        var x = min
        while (x <= max) {
            action.accept(x)
            x += step
        }
    }

}