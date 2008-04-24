/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.management;

// START SNIPPET: class
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.container.lifecycle.Started;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.management.annotation.ManagedAttribute;
import org.codehaus.cake.service.executor.ExecutorsService;

public class ScheduledClearManagement implements Manageable {
    private long scheduleMs;

    private Runnable runnable;

    private ScheduledExecutorService ses;

    private ScheduledFuture sf;

    public synchronized long getClearScheduleMs() {
        return scheduleMs;
    }

    public synchronized void manage(ManagedGroup parent) {
        parent.addChild("ClearCache", "Controls Clearing of the cache").add(this);
    }

    @ManagedAttribute(defaultValue = "clearMS", description = "The delay between clearings of the cache in Milliseconds")
    public synchronized void setClearScheduleMs(long scheduleMs) {
        if (sf != null) { // cancel existing scheduled clearing
            sf.cancel(false);
        }
        this.scheduleMs = scheduleMs;
        sf = ses.schedule(runnable, scheduleMs, TimeUnit.MILLISECONDS);
    }

    @Started
    public synchronized void started(final Cache<?, ?> cache, ExecutorsService executors) {
        ses = executors.getScheduledExecutorService(this);
        runnable = new Runnable() {
            public void run() {
                cache.clear();
            }
        };
        setClearScheduleMs(60 * 60 * 1000);// default 1 hour
    }
}
// END SNIPPET: class
