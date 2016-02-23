package frc.team5333.webui.api

import com.grack.nanojson.JsonObject
import frc.team5333.core.control.strategy.StrategyController
import frc.team5333.core.systems.Systems
import frc.team5333.webui.WebHandler
import spark.Request
import spark.Response
import java.util.*

class DriveAPI : API {

    override fun address(): String? = "drive"

    override fun init() { }

    override fun handle(request: Request?, response: Response?): Any? {
        var mode = Systems.control.driveMode().name
        var strat = StrategyController.INSTANCE.getStrategy().javaClass.canonicalName
        var map: HashMap<String, String> = hashMapOf(
                Pair("drive_mode", mode),
                Pair("strategy", strat)
        )
        return WebHandler.jsonToString(JsonObject(map))
    }
}