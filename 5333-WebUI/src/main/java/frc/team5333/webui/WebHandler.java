package frc.team5333.webui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static spark.Spark.*;

public class WebHandler {

    public static void init() {
        port(5801);

        get("/resources/:thing", (req, res) -> {
            return resource("resources/" + req.params(":thing"));
        });
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
