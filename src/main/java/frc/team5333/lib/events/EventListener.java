package frc.team5333.lib.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An EventListener is an annotation placed above a Method to declare it as 'listening' for events on the Event Bus.
 * You may specify a priority to change the execution order of listeners, and event allow for cancelled events to still
 * be processed
 *
 * @author Jaci
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface EventListener {

    /**
     * The priority of the event
     */
    public EventPriority priority() default EventPriority.NORMAL;

    /**
     * Allow cancelled events?
     */
    public boolean allowCancelled() default false;

    /**
     * Respect Event Class inheritance? If no, this will only trigger on the class of the event itself, and not
     * any subclasses. In most cases, you want this to be true.
     */
    public boolean respectsInheritance() default true;
}
