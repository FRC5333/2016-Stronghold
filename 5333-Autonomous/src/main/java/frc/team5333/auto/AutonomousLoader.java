package frc.team5333.auto;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import frc.team5333.core.Core;
import frc.team5333.core.control.strategy.Strategy;
import jaci.openrio.toast.lib.module.ModuleConfig;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AutonomousLoader {

    public static ArrayList<AutonomousMode> modes;
    public static HashMap<String, AutonomousMode.Portion> portions = new HashMap<>();

    public static void load() {
        try {
            modes = new ArrayList<>();
            Portions.INSTANCE.init();
            ModuleConfig config = Core.config;
            config.getString("autonomous.active", "{none}");

            JsonObject placeholder_obj = new JsonObject();
            placeholder_obj.put("name", "blank");
            JsonObject placeholder_portion = new JsonObject();
            placeholder_portion.put("type", "blank");
            JsonArray placeholder_portion_array = new JsonArray();
            placeholder_portion_array.add(placeholder_portion);
            placeholder_obj.put("portions", placeholder_portion_array);
            config.getOrDefault("autonomous.modes", new JsonObject[]{placeholder_obj});        // Placeholder

            FileInputStream str = new FileInputStream(config.parent_file);
            JsonObject rootObj = JsonParser.object().from(str).getObject("autonomous");
            str.close();
            JsonArray modes = rootObj.getArray("modes");
            if (modes != null) {
                for (int i = 0; i < modes.size(); i++) {
                    JsonObject mode = modes.getObject(i);
                    String name = mode.getString("name");
                    JsonArray portions = mode.getArray("portions");
                    ArrayList<Strategy> strats = new ArrayList<>();
                    for (int j = 0; j < portions.size(); j++) {
                        JsonObject portion = portions.getObject(j);
                        strats.add(AutonomousLoader.portions.get(portion.getString("type")).configure(portion));
                    }
                    AutonomousLoader.modes.add(new AutonomousMode(name, strats));
                }
            }
        } catch (Exception e) {
            Core.logger.error("Could not load Autonomous Configuration....");
            Core.logger.exception(e);
        }
    }

    public static void register(String name, AutonomousMode.Portion portion) {
        portions.put(name, portion);
    }

    public static AutonomousMode getActive() {
        String name = Core.config.getString("autonomous.active", "{none}");
        if (name.equalsIgnoreCase("{none}")) return null;
        return modes.stream().filter(m -> { return m.getName().equalsIgnoreCase(name); }).findFirst().get();
    }

}
