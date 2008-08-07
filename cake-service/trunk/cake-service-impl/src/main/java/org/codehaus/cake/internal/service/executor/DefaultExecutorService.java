package org.codehaus.cake.internal.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.Stoppable;

@UseInternals
public class DefaultExecutorService implements ServiceFactory<ExecutorService> {

    /** Default executor service. */
    private volatile ExecutorService defaultExecutor;
    /** Whether or not this service has been shutdown. */
    private boolean isShutdown;
    /** Lock for on-demand initialization of executors */
    private final Object poolLock = new Object();

    @Startable
    public void register(ServiceRegistrant registrant) {
        registrant.registerFactory(ExecutorService.class, this);
    }

    public ExecutorService lookup(
            org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<ExecutorService> context) {
        ExecutorService s = defaultExecutor;
        if (s == null) {
            synchronized (poolLock) {
                if (isShutdown) {
                    throw new IllegalStateException("This service has been shutdown");
                }
                s = defaultExecutor;
                if (s == null) {
                    defaultExecutor = s = Executors.newCachedThreadPool();
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
