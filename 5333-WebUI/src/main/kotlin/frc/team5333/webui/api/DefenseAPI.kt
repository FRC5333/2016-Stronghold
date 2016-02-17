package frc.team5333.webui.api

import frc.team5333.core.Core
import frc.team5333.core.data.DefenseInfo
import frc.team5333.core.data.MatchInfo
import spark.Request
import spark.Response
import kotlin.text.toInt

class DefenseAPI : API {

    override fun address(): String? = "defenses/set/:d1/:d2/:d3/:d4"

    override fun init() { }

    override fun handle(request: Request, response: Response?): Any? {
        DefenseInfo.configure(request.params(":d1"), request.params(":d2"), request.params(":d3"), request.params(":d4"))
        return "Done"
    }
}