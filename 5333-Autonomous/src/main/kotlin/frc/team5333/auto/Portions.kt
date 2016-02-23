package frc.team5333.auto

import com.grack.nanojson.JsonArray
import com.grack.nanojson.JsonObject
import frc.team5333.core.control.profiling.SplineSystem
import frc.team5333.core.control.strategy.*
import jaci.openrio.toast.lib.math.MathHelper

object Portions {
    fun init() {
        AutonomousLoader.register("align", AlignPortion())
        AutonomousLoader.register("shoot", ShootPortion())
        AutonomousLoader.register("profile", ProfilePortion())
        AutonomousLoader.register("blank", BlankPortion())
    }
}

class AlignPortion : AutonomousMode.Portion {
    override fun configure(obj: JsonObject): Strategy = StrategyAlign()
}

class ShootPortion: AutonomousMode.Portion {
    override fun configure(obj: JsonObject): Strategy {
        if (obj.containsKey("top") && obj.containsKey("bottom")) {
            return StrategyShoot(obj.getDouble("top"), obj.getDouble("bottom"))
        } else return StrategyShoot()
    }
}

class ProfilePortion: AutonomousMode.Portion {
    override fun configure(obj: JsonObject): Strategy {
        var wp = obj.getArray("waypoints")
        var points = wp.map {
            var wp_i: JsonArray = it as JsonArray
            SplineSystem.Waypoint(wp_i.getDouble(0), wp_i.getDouble(1), MathHelper.d2r(wp_i.getDouble(2)))
        }
        var pairs = SplineSystem.INSTANCE.generateTrajectoryPairs(points.toTypedArray())
        return StrategyMotionProfile(pairs);
    }
}

class BlankPortion: AutonomousMode.Portion {
    override fun configure(obj: JsonObject): Strategy = StrategyBlank()
}