package frc.team5333.auto

import com.grack.nanojson.JsonObject
import frc.team5333.core.Core
import frc.team5333.core.control.strategy.Strategy
import frc.team5333.core.control.strategy.StrategyController
import java.util.*

class AutonomousMode(var name: String, var portions: ArrayList<Strategy>) {

    interface Portion {
        fun configure(obj: JsonObject): Strategy
    }

    fun run() {
        portions.forEachIndexed { i, portion ->
            if (i != 0) {
                var last = portions.get(i - 1)
                last.then(portion)
            }
        }
        Core.logger.info("Running Autonomous: ${name}")
        StrategyController.INSTANCE.setStrategy(portions.first())
    }

}