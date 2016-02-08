package frc.team5333.webui.api

import com.grack.nanojson.JsonObject
import frc.team5333.core.control.ControlManager
import frc.team5333.core.teleop.TeleopController
import frc.team5333.webui.WebHandler
import spark.Request
import spark.Response
import java.util.*
import kotlin.collections.hashMapOf

class DriveAPI : API {

    override fun address(): String? = "drive"

    override fun init() { }

    override fun handle(request: Request?, response: Response?): Any? {
        var mode = ControlManager.INSTANCE.driveMode()
        var strat = TeleopController.INSTANCE.activeStrategy
        var map: HashMap<String, Any> = hashMapOf(
                Pair("drive_mode", mode),
                Pair("strategy", strat)
        )
        return WebHandler.jsonToString(JsonObject(map))
    }
}