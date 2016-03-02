package frc.team5333.core.data;

import frc.team5333.core.Core;
import frc.team5333.core.events.DefenseChangedEvent;
import frc.team5333.lib.events.EventBus;

import java.util.Arrays;

public class DefenseInfo {

    public static enum Category {
        A, B, C, D
    }

    public static enum Defense {
        PORTCULUS("prt", Category.A, "portculus"),     CHEVAL_DE_FRISE("che", Category.A, "cheval"),
        RAMPARTS("rmp", Category.B, "ramparts"),       MOAT("mot", Category.B, "moat"),
        DRAWBRIDGE("drw", Category.C, "drawbridge"),   SALLY_PORT("sly", Category.C, "sally"),
        ROCK_WALL("rck", Category.D, "rock"),          ROUGH_TERRAIN("rgh", Category.D, "rough");

        public String shortname, longname;
        public Category category;

        Defense(String shortname, Category category, String longname) {
            this.shortname = shortname; this.category = category; this.longname = longname;
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
        EventBus.INSTANCE.raiseEvent(new DefenseChangedEvent(defense_configuration));
    }

    public static int categoryIndex(Category cat) {
        for (int i = 0; i < 4; i++) {
            if (defense_configuration[i].category == cat) return i;
        }
        return 0;
    }

    /**
     * Translate a defense index (0-indexed) to a location on the field. Return value is in meters, perpendicular
     * to the field walls and parallel to the outer works and midline. This value is the centre of the defense on the
     * outer works.
     */
    public static double indexToFieldLocation(int index) {
        return (index + 1) * 1.27 + (1.27 / 2);     // 4ft 2in is 1.27m
    }

    public static Defense[] getDefenses() {
        return defense_configuration;
    }

}
