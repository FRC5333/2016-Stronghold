package frc.team5333.lib.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base class for Events. Extend this in your own Class to be able to raise events on the EventBus.
 */
public class EventBase {

    private boolean isCancelled;
    public long raiseTime;
    public long raiseEndTime;
    public double matchTimeStart;
    public boolean completed;

    private List<Consumer<EventBase>> _success = new ArrayList<>();
    private List<Consumer<EventBase>> _failed = new ArrayList<>();
    private List<Consumer<EventBase>> _completion = new ArrayList<>();
    private List<Consumer<EventBase>> _cancelled = new ArrayList<>();

    private Object _lock = new Object();

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
        if (cancel) _cancelled.forEach(c -> c.accept(this));

        isCancelled = cancel;
    }

    public synchronized EventBase onSuccess(Consumer<EventBase> run) {
        _success.add(run);
        return this;
    }

    public synchronized EventBase onFailed(Consumer<EventBase> run) {
        _failed.add(run);
        return this;
    }

    public synchronized EventBase onCompletion(Consumer<EventBase> run) {
        _completion.add(run);
        return this;
    }

    public synchronized EventBase onCancelled(Consumer<EventBase> run) {
        _cancelled.add(run);
        return this;
    }

    public EventBase waitForCompletion() {
        if (completed) return this;
        synchronized (_lock) {
            try {
                _lock.wait();
            } catch (InterruptedException e) {
            }
            return this;
        }
    }

    public void completeDispatch() {
        synchronized (_lock) {
            _completion.forEach(c -> c.accept(this));
            if (!isCancelled) _success.forEach(c -> c.accept(this));
            if (isCancelled) _failed.forEach(c -> c.accept(this));
            _lock.notifyAll();
        }
    }

    public String getCustomData() {
        return  "";
    }

}
