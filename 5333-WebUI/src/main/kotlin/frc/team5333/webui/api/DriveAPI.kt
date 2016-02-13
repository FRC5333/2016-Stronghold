package frc.team5333.webui.api

import com.grack.nanojson.JsonObject
import frc.team5333.core.control.ControlManager
import frc.team5333.core.control.strategy.StrategyController
import frc.team5333.webui.WebHandler
import spark.Request
import spark.Response
import java.util.*
import kotlin.collections.hashMapOf

class DriveAPI : API {

    override fun address(): String? = "drive"

    override fun init() { }

    override fun handle(request: Request?, response: Response?): Any? {
        var mode = ControlManager.INSTANCE.driveMode().name
        var strat = StrategyController.INSTANCE.activeStrategy.javaClass.canonicalName
        var map: HashMap<String, String> = hashMapOf(
                Pair("drive_mode", mode),
                Pair("strategy", strat)
        )
        return WebHandler.jsonToString(JsonObject(map))
    }
}