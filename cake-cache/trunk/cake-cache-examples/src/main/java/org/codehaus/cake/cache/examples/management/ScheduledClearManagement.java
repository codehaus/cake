/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.management;

// START SNIPPET: class
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.Caches;
import org.codehaus.cake.management.ManagedAttribute;
import org.codehaus.cake.management.ManagedObject;
import org.codehaus.cake.service.RunAfter;
import org.codehaus.cake.service.Container.State;
@ManagedObject(defaultValue = "ClearCache", description = "Controls Clearing of the cache")
public class ScheduledClearManagement {
    private long scheduleMs;

    private Runnable runnable;

    private ScheduledExecutorService ses;

    private ScheduledFuture<?> sf;

    public synchronized long getClearScheduleMs() {
        return scheduleMs;
    }

    @ManagedAttribute(defaultValue = "clearMS", description = "The delay between clearings of the cache in Milliseconds")
    public synchronized void setClearScheduleMs(long scheduleMs) {
        if (sf != null) { // cancel existing scheduled clearing
            sf.cancel(false);
        }
        this.scheduleMs = scheduleMs;
        sf = ses.scheduleAtFixedRate(runnable, scheduleMs, scheduleMs, TimeUnit.MILLISECONDS);
    }

    @RunAfter(State.RUNNING)
    public synchronized void started(final Cache<?, ?> cache) {
        ses = cache.with().scheduledExecutor();
        runnable = Caches.clearAsRunnable(cache);
        setClearScheduleMs(60 * 1000);// default 1 minute
    }
}
// END SNIPPET: class
