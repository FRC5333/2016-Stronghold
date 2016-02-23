package frc.team5333.webui.controller

import frc.team5333.webui.WebHandler
import spark.Request
import spark.Response
import java.util.*

class TrainingController : Controller {
    override fun address(): String = "/train"

    override fun init() { }

    override fun handle(request: Request?, response: Response?): Any? {
        var map: HashMap<String, Any> = hashMapOf( )
        return WebHandler.applyTemplate("train.html", map)
    }
}