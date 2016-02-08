package frc.team5333.webui.controller

import frc.team5333.core.data.MatchInfo
import frc.team5333.webui.WebHandler
import spark.Request
import spark.Response
import java.util.*
import kotlin.collections.hashMapOf
import kotlin.text.toLowerCase

class IndexController : Controller{
    override fun address(): String = "/"

    override fun init() { }

    override fun handle(request: Request?, response: Response?): Any? {
        var map: HashMap<String, Any> = hashMapOf(
                Pair("match_number", MatchInfo.matchNum),
                Pair("match_type", MatchInfo.matchType.shortName.toLowerCase())
        )
        return WebHandler.applyTemplate("index.html", map)
    }
}