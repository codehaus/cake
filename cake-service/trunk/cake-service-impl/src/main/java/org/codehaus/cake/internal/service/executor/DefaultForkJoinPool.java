package org.codehaus.cake.internal.service.executor;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.codehaus.cake.forkjoin.ForkJoinPool;
import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.Stoppable;

@UseInternals
public class DefaultForkJoinPool implements ServiceFactory<ForkJoinExecutor> {

    /** Default executor service. */
    private volatile ForkJoinPool defaultExecutor;
    /** Whether or not this service has been shutdown. */
    private boolean isShutdown;
    /** Lock for on-demand initialization of executors */
    private final Object poolLock = new Object();
    
    @Startable
    public void register(ServiceRegistrant registrant) {
        registrant.registerFactory(ForkJoinExecutor.class, this);
    }

    public ForkJoinExecutor lookup(org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<ForkJoinExecutor> context) {
        ForkJoinPool e = defaultExecutor;
        if (e == null) {
            synchronized (poolLock) {
                if (isShutdown) {
                    throw new IllegalStateException("This service has been shutdown");
                }
                e = defaultExecutor;
                if (e == null) {
                    // use ceil(7/8 * ncpus)
                    int nprocs = Runtime.getRuntime().availableProcessors();
                    int nthreads = nprocs - (nprocs >>> 3);
//                    System.out.println("Starting");
                    defaultExecutor = e = new ForkJoinPool(nthreads);
                }
            }
        }
        return e;
    }

    @Stoppable
    public void shutdown() throws InterruptedException {
        // TODO fix async shutdown
        synchronized (poolLock) {
            isShutdown = true;
            if (defaultExecutor != null) {
                defaultExecutor.shutdown();
//                System.out.println("Stopping");
            }
            if (defaultExecutor != null) {
                defaultExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }
        }
    }
}
