package frc.team5333.lib.util;

import jaci.openrio.toast.core.thread.Async;
import jaci.openrio.toast.core.thread.AsyncTask;

public class AsyncFunctional {

    public static AsyncTask schedule(Runnable run) {
        AsyncTask task = run::run;
        Async.schedule(task);
        return task;
    }

}
