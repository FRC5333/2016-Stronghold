package frc.team5333.lib.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
     * Respect Event Class inheritance?
     */
    public boolean respectsInheritance() default true;
}
