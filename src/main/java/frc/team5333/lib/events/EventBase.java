package frc.team5333.lib.events;

/**
 * Base class for Events. Extend this in your own Class to be able to raise events on the EventBus.
 */
public class EventBase {

    private boolean isCancelled;

    /**
     * Is this event cancelled?
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Set this event to be cancelled
     */
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

}
