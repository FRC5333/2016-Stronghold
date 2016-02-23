package frc.team5333.core.systems

import jaci.openrio.toast.core.io.Storage
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.util.*

class ShooterLookup {

    var entries: ArrayList<Triple<Double, Double, Double>> = arrayListOf()
    var filename = "system/shooter/lookup.csv"

    fun loadFromFile() {
        var file = Storage.highestPriority(filename)
        file.parentFile.mkdirs()

        if (file.exists()) {
            clearEntries()
            var reader = BufferedReader(FileReader(file))
            reader.readLine()
            reader.readText().split("\n").forEach {
                var spl = it.split(",").map { it.toDouble() }
                addEntry(spl[0], spl[1], spl[2])
            }
            reader.close()
        }
    }

    fun saveToFile() {
        var file = Storage.highestPriority(filename)
        file.parentFile.mkdirs()
        file.createNewFile()

        var writer = FileWriter(file)
        writer.write("height,throttleTop,throttleBottom\n")
        entries.forEach {
            writer.write("${it.first},${it.second},${it.third}\n")
        }
        writer.close()
    }

    fun sort() {
        entries.sort { a, b ->
            if (a.first > b.first)
                -1
            else
                1
        }
    }

    fun addEntry(distance: Double, throttleTop: Double, throttleBottom: Double) {
        entries.filter { Math.abs(it.first - distance) < 0.025 }.forEach { entries.remove(it) }
        entries.add(Triple(distance, throttleTop, throttleBottom))
        sort()
    }

    fun get(distance: Double): Pair<Double, Double> {
        if (distance <= entries.first().first) return Pair(0.0, 0.0)
        if (distance >= entries.last().first) return Pair(1.0, 1.0)

        var f: Triple<Double, Double, Double> = Triple(0.0, 0.0, 0.0)
        var s: Triple<Double, Double, Double> = Triple(0.0, 0.0, 0.0)
        entries.forEachIndexed { i, it ->
            if (entries.size != i) {
                var next = entries.get(i + 1)
                if (distance >= it.first && distance <= next.first) {
                    f = it
                    s = next
                }
            }
        }

        var delta_dist = s.first - f.first
        var gradient_top = (s.second - f.second) / delta_dist
        var gradient_bottom = (s.third - f.third) / delta_dist

        var top_throttle = gradient_top * ( distance - f.first ) + f.second
        var bottom_throttle = gradient_bottom  * ( distance - f.first ) + f.third

        return Pair(top_throttle, bottom_throttle)
    }

    fun clearEntries() {
        entries = arrayListOf()
    }

}