package frc.team5333.webui;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonWriter;
import frc.team5333.core.Core;
import frc.team5333.webui.api.API;
import frc.team5333.webui.api.DriveAPI;
import frc.team5333.webui.api.EventBusAPI;
import frc.team5333.webui.api.MatchAPI;
import frc.team5333.webui.controller.Controller;
import frc.team5333.webui.controller.IndexController;
import frc.team5333.webui.websockets.SocketLogger;
import jaci.openrio.toast.lib.log.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.webSocket;

public class WebHandler {

    static Handlebars hb;
    static HashMap<String, Template> templates;

    public static void init() {
        hb = new Handlebars();
        templates = new HashMap<>();

        hb.registerHelper("import", (ctx, opt) -> {
            HashMap<String, Object> hm = (HashMap<String, Object>) opt.hash;
            String template = (String) hm.get("t");
            hm.remove("t");
            if (ctx instanceof HashMap) hm.putAll((Map<? extends String, ?>) ctx);
            return new Handlebars.SafeString(applyTemplate(template, hm));
        });

        hb.registerHelper("include", (ctx, opt) -> {
            HashMap<String, Object> hm = (HashMap<String, Object>) opt.hash;
            String template = (String) hm.get("t");
            hm.remove("t");
            if (ctx instanceof HashMap) hm.putAll((Map<? extends String, ?>) ctx);
            hm.put("include_body", new Handlebars.SafeString(
                    hb.compileInline(
                            opt.fn.text()
                    ).apply(hm)));
            return new Handlebars.SafeString(applyTemplate(template, hm));
        });

        port(5801);

        webSocket("/socket/logger", SocketLogger.class);
        Logger.addHandler(new SocketLogger());

        get("/styles/:file", (req, res) -> {
            res.type("text/css");
            return resource("resources/style/" + req.params(":file"));
        });

        get("/png/:file", (req, res) -> {
            res.type("image/png");
            return resourceRaw("resources/img/" + req.params(":file"));
        });

        get("/svg/:file", (req, res) -> {
            res.type("image/svg+xml");
            return resource("resources/img/" + req.params(":file"));
        });

        get("/scripts/:file", (req, res) -> {
            res.type("text/javascript");
            return resource("resources/script/" + req.params(":file"));
        });

        get("/ping", (res, req) -> { return "pong"; });

        register(new EventBusAPI());
        register(new DriveAPI());
        register(new MatchAPI());

        register(new IndexController());
    }

    public static Handlebars getHandlebars() {
        return hb;
    }

    public static void register(API api) {
        api.init();
        get(api.address(), api::handle);
    }

    public static void register(Controller controller) {
        controller.init();
        get(controller.address(), controller::handle);
    }

    public static String jsonToString(JsonObject obj) {
        StringWriter writer = new StringWriter();
        JsonWriter.indent("\t").on(writer).value(obj).done();
        return writer.toString();
    }

    public static String jsonToString(JsonArray obj) {
        StringWriter writer = new StringWriter();
        JsonWriter.indent("\t").on(writer).value(obj).done();
        return writer.toString();
    }

    public static String resource(String name) {
//        try(BufferedReader reader = new BufferedReader(new InputStreamReader(WebHandler.class.getResourceAsStream("/5333/webui/" + name)))) {
        try(BufferedReader reader = new BufferedReader(new FileReader("D:\\Programming\\FRC\\5333\\2016\\2016-Stronghold\\5333-WebUI\\src\\main\\resources\\5333\\webui\\" + name))) {
            String ln;
            String total = "";
            while ((ln = reader.readLine()) != null) {
                total += ln + "\n";
            }
            reader.close();
            return total;
        } catch (Exception e) {
            return "";
        }
    }

    public static byte[] resourceRaw(String name) {
        try(InputStream in = new FileInputStream("D:\\Programming\\FRC\\5333\\2016\\2016-Stronghold\\5333-WebUI\\src\\main\\resources\\5333\\webui\\" + name)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = in.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static String applyTemplate(String name, HashMap<String, Object> values) {
        if (true) {
//        if (!templates.containsKey(name)) {
            String path = "templates/" + name;
            try {
                templates.put(name, getHandlebars().compileInline(resource(path)));
            } catch (IOException e) {
                Core.logger.exception(e);
                return "500 - Oops, Template could not be loaded :c";
            }
        }
        try {
            return templates.get(name).apply(values);
        } catch (IOException e) {
            Core.logger.exception(e);
            return "500 - Oops, Template could not be applied :c";
        }
    }
}
