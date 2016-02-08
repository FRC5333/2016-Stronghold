package frc.team5333.webui;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonWriter;
import frc.team5333.webui.api.API;
import frc.team5333.webui.api.DriveAPI;
import frc.team5333.webui.api.EventBusAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;

import static spark.Spark.get;
import static spark.Spark.port;

public class WebHandler {

    public static void init() {
        port(5801);

        get("/resources/:thing", (req, res) -> {
            return resource("resources/" + req.params(":thing"));
        });

        register(new EventBusAPI());
        register(new DriveAPI());
    }

    public static void register(API api) {
        api.init();
        get(api.address(), api::handle);
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
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(WebHandler.class.getResourceAsStream("/5333/webui/" + name)))) {
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

}
