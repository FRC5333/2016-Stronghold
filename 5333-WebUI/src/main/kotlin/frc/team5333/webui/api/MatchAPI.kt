package frc.team5333.webui.api

import frc.team5333.core.data.MatchInfo
import spark.Request
import spark.Response

class MatchAPI : API {

    override fun address(): String? = "matches/set/:type/:num"

    override fun init() { }

    override fun handle(request: Request, response: Response?): Any? {
        var type = request.params(":type")
        var num = request.params(":num").toInt()

        MatchInfo.set(type, num)
        return "Done"
    }
}