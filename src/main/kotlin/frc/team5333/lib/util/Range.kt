package frc.team5333.lib.util

import java.util.function.Consumer
import kotlin.ranges.rangeTo

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