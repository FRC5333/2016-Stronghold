package frc.team5333.core.data;

import frc.team5333.core.Core;
import frc.team5333.core.events.PlacementChangedEvent;
import frc.team5333.lib.events.EventBus;

public class PlacementInfo {

    // Our robot is always placed such that the center of the chassis is inline with a boulder. This makes it
    // easy for placement and determining where the robot is on the field.
    public static int boulder_inline = 0;

    public static void update(int boulder_index) {
        boulder_inline = boulder_index;
        Core.logger.info("Placement Updated! Boulder Index: " + boulder_index);
        EventBus.INSTANCE.raiseEvent(new PlacementChangedEvent());
    }

    /**
     * Translate the boulder index we are lined up with to actual meters from the field Guard Rail.
     */
    public static double fieldLocation() {
        return 1.1557 * boulder_inline;
    }

}
