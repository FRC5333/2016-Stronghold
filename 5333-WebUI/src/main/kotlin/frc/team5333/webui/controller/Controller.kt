package frc.team5333.webui.controller

import spark.Route

interface Controller : Route {

    fun address(): String

    fun init()

}