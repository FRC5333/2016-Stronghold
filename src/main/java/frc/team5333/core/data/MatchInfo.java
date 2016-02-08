package frc.team5333.core.data;

import frc.team5333.core.Core;
import jaci.openrio.toast.core.io.Persistent;
import jaci.openrio.toast.core.io.Storage;

/**
 * The MatchInfo class contains data about the current match we are in. This is used to record our Robot Statistics
 * and performance over time, and be able to review this data after each match and publish it.
 *
 * @author Jaci
 */
public class MatchInfo {

    public static enum MatchType {
        QUALIFICATIONS("Quals"), QUARTER_FINALS("Quarter"), SEMI_FINALS("Semi"), FINALS("Final");

        String shortName;

        MatchType(String shortName) {
            this.shortName = shortName;
        }

        public String getShortName() {
            return shortName;
        }
    }

    static Persistent persistent;

    public static MatchType matchType;
    public static int matchNum;

    public static void load() {
        persistent = new Persistent(Storage.highestPriority("game/Match.json"));
        if (!persistent.valueExists("type") || !persistent.valueExists("num")) {
            Core.logger.info("[MATCH] No persistent match data file! Creating a new one!");
            persistent.setString("type", MatchType.QUALIFICATIONS.name());
            persistent.setNumber("num", 1);
            persistent.save();
        }

        matchType = MatchType.valueOf(persistent.getString("type"));
        matchNum = persistent.getInteger("num");

        Core.logger.info("[MATCH] Current Match: " + matchType.getShortName() + " #" + matchNum);
    }

    public static void set(String type, int num) {
        for (MatchType ty : MatchType.values())
            if (ty.getShortName().equalsIgnoreCase(type)) matchType = ty;
        matchNum = num;

        persistent.setString("type", matchType.name());
        persistent.setNumber("num", num);
        persistent.save();

        load();
    }

}
