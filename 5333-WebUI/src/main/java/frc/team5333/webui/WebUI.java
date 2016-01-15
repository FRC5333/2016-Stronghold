package frc.team5333.webui;

import jaci.openrio.toast.lib.module.ToastModule;

public class WebUI extends ToastModule {

    @Override
    public String getModuleName() {
        return "5333-WebUI";
    }

    @Override
    public String getModuleVersion() {
        return "0.1.0";
    }

    @Override
    public void prestart() {
        WebHandler.init();
    }

    @Override
    public void start() {}
}
