package org.codehaus.cake.internal.service.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.annotation.ExportAsService;
import org.codehaus.cake.service.annotation.Startable;
import org.codehaus.cake.service.annotation.Stoppable;

@UseInternals
@ExportAsService(ScheduledExecutorService.class)
public class DefaultScheduledExecutorService implements ServiceFactory<ScheduledExecutorService> {

    /** Default executor service. */
    private volatile ScheduledExecutorService defaultExecutor;
    /** Whether or not this service has been shutdown. */
    private boolean isShutdown;
    /** Lock for on-demand initialization of executors */
    private final Object poolLock = new Object();

    public ScheduledExecutorService lookup(
            org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<ScheduledExecutorService> context) {
        ScheduledExecutorService s = defaultExecutor;
        if (s == null) {
            synchronized (poolLock) {
                if (isShutdown) {
                    throw new IllegalStateException("This service has been shutdown");
                }
                s = defaultExecutor;
                if (s == null) {
                    defaultExecutor = s = Executors.newSingleThreadScheduledExecutor();
                }
            }
        }
        return s;
    }

    @Stoppable
    public void shutdown() throws InterruptedException {
        // TODO fix async shutdown
        synchronized (poolLock) {
            isShutdown = true;
            if (defaultExecutor != null) {
                defaultExecutor.shutdown();
            }
            if (defaultExecutor != null) {
                defaultExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }
        }
    }
}
