package frc.team5333.lib.events;

/**
 * An EventPriority determines the Execution order of events raised on the Event Bus. EventListeners with a Higher Priority
 * are called first. The default priority is 'normal'
 *
 * @author Jaci
 */
public enum EventPriority {
    HIGHEST,    //Triggered First
    HIGHER,
    HIGH,
    NORMAL,
    LOW,
    LOWER,
    LOWEST      //Triggered Last
}