package frc.team5333.core.data;

import frc.team5333.core.Core;

import java.util.Arrays;

public class DefenseInfo {

    public static enum Defense {
        PORTCULUS("prt", "a", "portculus"),     CHEVAL_DE_FRISE("che", "a", "cheval"),
        RAMPARTS("rmp", "b", "ramparts"),       MOAT("mot", "b", "moat"),
        DRAWBRIDGE("drw", "c", "drawbridge"),   SALLY_PORT("sly", "c", "sally"),
        ROCK_WALL("rck", "d", "rock"),          ROUGH_TERRAIN("rgh", "d", "rough");

        public String shortname, group, longname;

        Defense(String shortname, String group, String longname) {
            this.shortname = shortname; this.group = group; this.longname = longname;
        }
    }

    public static Defense[] defense_configuration = new Defense[4];
    public static boolean configured = false;

    public static void configure(String... longnames) {
        for (int i = 0; i < 4; i++) {
            Defense sel = null;
            for (Defense def : Defense.values())
                if (def.longname.equalsIgnoreCase(longnames[i]))
                    sel = def;
            defense_configuration[i] = sel;
        }
        configured = true;
        Core.logger.info("Defenses Set To: " + Arrays.toString(defense_configuration));
    }

    public static Defense[] getDefenses() {
        return defense_configuration;
    }

}
