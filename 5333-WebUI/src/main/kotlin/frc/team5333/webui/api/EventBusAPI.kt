package frc.team5333.webui.api

import com.grack.nanojson.JsonArray
import com.grack.nanojson.JsonObject
import com.grack.nanojson.JsonWriter
import frc.team5333.lib.events.EventBase
import frc.team5333.lib.events.EventBus
import frc.team5333.lib.events.EventListener
import frc.team5333.lib.util.FifoBuffer
import frc.team5333.webui.WebHandler
import spark.Request
import spark.Response
import java.io.StringWriter
import kotlin.ranges.downTo

class EventBusAPI : API{

    var bases: FifoBuffer<EventBase> = FifoBuffer(20)

    override fun address(): String? = "events"

    override fun init() {
        EventBus.INSTANCE.register(this)
    }

    @EventListener(respectsInheritance = true, allowCancelled = true)
    fun onEvent(e: EventBase) = bases.add(e)

    override fun handle(request: Request?, response: Response?): Any? {
        val array = JsonArray()
        for (i in 19 downTo 0) {
            val base = bases.get(i)
            if (base != null) {
                val obj = JsonObject()
                obj.put("class", base.javaClass.canonicalName)
                obj.put("string", base.toString())
                obj.put("startTime", base.raiseTime)
                obj.put("endTime", base.raiseEndTime)
                obj.put("duration", base.raiseEndTime - base.raiseTime)
                array.add(obj)
            }
        }
        return WebHandler.jsonToString(array)
    }
}
