package frc.team5333.lib.events;

import frc.team5333.core.Core;
import jaci.openrio.toast.core.thread.Async;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;

/**
 * The EventBus provides a listener system for listening to events. Events are raised, and then any available
 * listeners are triggered with an instance of the event. The event may be cancelled, at which point the pipeline
 * stops unless the EventListener is specifies that is wants to listen to cancelled events. EventListeners may
 * have a priority, in which the execution order of events is determined.
 *
 * @author Jaci
 */
public enum EventBus {
    INSTANCE;

    private ArrayList<Object> listeners = new ArrayList<Object>();

    private boolean exclusive = false;

    /**
     * Register either a Class or Instance of an Object as a handler for the eventsystem
     * <p/>
     * Instances will allow you to access non-static methods
     * Classes can only access Static methods
     */
    public void register(Object object) {
        listeners.add(object);
    }

    /**
     * Unregister an object from the handlers list
     */
    public void unregister(Object object) {
        listeners.remove(object);
    }

    /**
     * Unregister all handlers of this type
     */
    public void unregisterAllOfType(Class type) {
        for (Object curr : listeners.toArray()) {
            Class currClass;
            if (curr instanceof Class)
                currClass = (Class) curr;
            else currClass = curr.getClass();

            if (currClass == type)
                listeners.remove(curr);

        }
    }

    /**
     * Exclusivity setting.
     * <p/>
     * Set this to true if you want instances to ONLY access non-static methods. By default, this is set to false, meaning instances can access static methods
     */
    public void setExclusive(boolean val) {
        exclusive = val;
    }

    /**
     * Get the listener list in the form of a read-only list
     */
    public List<Object> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * Raise an event to all the available listeners in a new thread
     */
    public void raiseEventConcurrent(final EventBase event) {
        new Thread() {
            public void run() {
                try {
                    raise(event);
                } catch (Exception e) {
                    Core.logger.exception(e);
                }
            }
        }.start();
    }

    /**
     * Raise an event on the Async Thread Pool. This will execute when resources become available
     * @param event
     */
    public void raiseEventAsync(final EventBase event) {
        Async.schedule(() -> {
            try {
                raise(event);
            } catch (Exception e) {
                Core.logger.exception(e);
            }
        });
    }

    /**
     * Raise an event to all available listeners
     * @return false if the event was cancelled
     */
    public boolean raiseEvent(final EventBase event) {
        try {
            raise(event);
        } catch (Exception e) {
            Core.logger.exception(e);
        }
        return !event.isCancelled();
    }

    private void raise(final EventBase event) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for (Object currentHandler : (ArrayList) listeners.clone()) {
            Method[] methods;
            boolean isClass = false;


            if (currentHandler instanceof Class) {
                methods = ((Class) currentHandler).getMethods();
                isClass = true;
            } else {
                methods = currentHandler.getClass().getMethods();
            }

            ArrayList<Method> valid = new ArrayList<Method>();

            for (Method method : methods) {
                EventListener annotation = method.getAnnotation(EventListener.class);
                if (annotation == null)
                    continue;

                Class[] params = method.getParameterTypes();

                if (params.length != 1)
                    continue;

                if (annotation.respectsInheritance() && !params[0].isAssignableFrom(event.getClass()))
                    continue;

                if (!annotation.respectsInheritance() && params[0] != event.getClass())
                    continue;


                if (!isClass && exclusive && Modifier.isStatic(method.getModifiers())) continue;

                if (isClass && !Modifier.isStatic(method.getModifiers())) continue;

                valid.add(method);
            }

            Comparator<Method> comp = new Comparator<Method>() {
                @Override
                public int compare(Method o1, Method o2) {
                    EventListener a1 = o1.getAnnotation(EventListener.class);
                    EventListener a2 = o2.getAnnotation(EventListener.class);

                    if (a1.priority().ordinal() < a2.priority().ordinal())
                        return -1;

                    if (a1.priority().ordinal() > a2.priority().ordinal())
                        return 1;

                    return 0;
                }

                @Override
                public boolean equals(Object obj) {
                    return false;
                }
            };

            Collections.sort(valid, comp);

            for (Method method : valid) {
                EventListener annotation = method.getAnnotation(EventListener.class);

                if (event.isCancelled()) {
                    if (annotation.allowCancelled())
                        method.invoke(currentHandler, event);
                } else
                    method.invoke(currentHandler, event);
            }

        }
    }


}
