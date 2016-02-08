package frc.team5333.webui.api;

import spark.Route;

public interface API extends Route {

    public String address();

    public void init();

}
