package org.codehaus.cake.internal.service.executor;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.service.ExportAsService;
import org.codehaus.cake.service.OnShutdown;
import org.codehaus.cake.service.ServiceProvider;
import org.codehaus.cake.util.concurrent.ForkJoinPool;

@UseInternals
@ExportAsService(ForkJoinPool.class)
public class DefaultForkJoinPool implements ServiceProvider<ForkJoinPool> {

    /** Default executor service. */
    private volatile ForkJoinPool defaultExecutor;

    /** Whether or not this service has been shutdown. */
    private boolean isShutdown;

    /** Lock for on-demand initialization of executors */
    private final Object poolLock = new Object();

    public ForkJoinPool lookup(org.codehaus.cake.service.ServiceProvider.ServiceFactoryContext<ForkJoinPool> context) {
        ForkJoinPool e = defaultExecutor;
        if (e == null) {
            synchronized (poolLock) {
                if (isShutdown) {
                    throw new IllegalStateException("This service has been shutdown, cannot acquire executor");
                }
                e = defaultExecutor;
                if (e == null) {
                    // use ceil(7/8 * ncpus)
                    int nprocs = Runtime.getRuntime().availableProcessors();
                    int nthreads = nprocs - (nprocs >>> 3);
                    // System.out.println("Starting");
                    defaultExecutor = e = new ForkJoinPool(nthreads);
                }
            }
        }
        return e;
    }

    @OnShutdown
    public void shutdown() throws InterruptedException {
        // TODO fix async shutdown
        synchronized (poolLock) {
            isShutdown = true;
            if (defaultExecutor != null) {
                defaultExecutor.shutdown();
                // System.out.println("Stopping");
            }
            if (defaultExecutor != null) {
                defaultExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }
        }
    }
}
