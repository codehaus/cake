package org.codehaus.cake.internal.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.service.ExportAsService;
import org.codehaus.cake.service.RunAfter;
import org.codehaus.cake.service.ServiceProvider;
import org.codehaus.cake.service.Container.State;

@UseInternals
@ExportAsService(ExecutorService.class)
public class DefaultExecutorService implements ServiceProvider<ExecutorService> {

    /** Default executor service. */
    private volatile ExecutorService defaultExecutor;
    /** Whether or not this service has been shutdown. */
    private boolean isShutdown;
    /** Lock for on-demand initialization of executors */
    private final Object poolLock = new Object();

    public ExecutorService lookup(
            org.codehaus.cake.service.ServiceProvider.Context<ExecutorService> context) {
        ExecutorService s = defaultExecutor;
        if (s == null) {
            synchronized (poolLock) {
                if (isShutdown) {
                    throw new IllegalStateException("This service has been shutdown");
                }
                s = defaultExecutor;
                if (s == null) {
                    ThreadPoolExecutor tpe = new ThreadPoolExecutor(2000, 3000, 60L, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>());
//                    tpe=new ThreadPoolExecutor(0, 3000, 60L, TimeUnit.SECONDS,
//                            new SynchronousQueue<Runnable>());
                    defaultExecutor = s = tpe;
                }
            }
        }
        return s;
    }

    @RunAfter(State.SHUTDOWN)
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
