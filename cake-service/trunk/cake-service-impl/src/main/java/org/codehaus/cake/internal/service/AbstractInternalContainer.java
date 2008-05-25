package org.codehaus.cake.internal.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.service.spi.ContainerInfo;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceManager;

public abstract class AbstractInternalContainer implements Container {

    private final String name;;

    private final RunState runState;

    private final ServiceManager sm;

    public AbstractInternalContainer(Composer composer) {
        ContainerInfo info = composer.get(ContainerInfo.class);
        if (!composer.hasService(Container.class)) {
            composer.registerInstance(Container.class, this);
            composer.registerInstance(info.getContainerType(), this);
        }
        name = info.getContainerName();
        sm = composer.get(ServiceManager.class);
        runState = composer.get(RunState.class);
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return runState.awaitTermination(timeout, unit);
    }

    /** {@inheritDoc} */
    public Map<Class<?>, Object> getAllServices() {
        lazyStart();
        return sm.getAllServices();
    }

    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public final <T> T getService(Class<T> serviceType) {
        lazyStart();
        return sm.getService(serviceType);
    }

    /** {@inheritDoc} */
    public final boolean hasService(Class<?> type) {
        lazyStart();
        return sm.hasService(type);
    }

    public boolean isShutdown() {
        return runState.isAtLeastShutdown();
    }

    public boolean isStarted() {
        return runState.isAtLeastRunning();
    }

    public boolean isTerminated() {
        return runState.isTerminated();
    }

    protected void lazyStart() {
        runState.isRunningLazyStart(false);
    }

    protected void lazyStartFailIfShutdown() {
        runState.isRunningLazyStart(true);
    }

    public void shutdown() {
        runState.shutdown(false);
    }

    public void shutdownNow() {
        runState.shutdown(true);
    }
}
