package org.codehaus.cake.internal.service.executor;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.executor.ExecutorsManager;
import org.codehaus.cake.service.executor.ExecutorsService;

public class DefaultExecutorsService implements ExecutorsService, CompositeService {
    private final ExecutorsManager manager;

    // cache loader executor
    // call getExecutorService(CacheLoader cl)
    // call getExecutorService(CacheLoaderService)
    // call getExecutorService(Cache c)
    // call getExecutorService(DEFAULT)

    public DefaultExecutorsService(ExecutorsConfiguration conf) {
        ExecutorsManager manager = conf.getExecutorManager();
        if (manager == null) {
            manager = new DefaultExecutorsManager();
        }
        this.manager = manager;
    }

    public ExecutorService getExecutorService() {
        return getExecutorService(null);
    }

    public ExecutorService getExecutorService(Object service) {
        return getExecutorService(service, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public ExecutorService getExecutorService(Object service, AttributeMap attributes) {
        return manager.getExecutorService(service, attributes);
    }

    public ForkJoinExecutor getForkJoinExecutor() {
        return getForkJoinExecutor(null);
    }

    public ForkJoinExecutor getForkJoinExecutor(Object service) {
        return getForkJoinExecutor(service, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes) {
        return manager.getForkJoinExecutor(service, attributes);

    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return getScheduledExecutorService(null);
    }

    public ScheduledExecutorService getScheduledExecutorService(Object service) {
        return getScheduledExecutorService(service, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes) {
        return manager.getScheduledExecutorService(service, attributes);
    }

    @Startable
    public void register(ContainerConfiguration<?> configuration, ServiceRegistrant serviceRegistrant) throws Exception {
        serviceRegistrant.registerService(ExecutorsService.class, this);
    }

    @Override
    public Collection<?> getChildServices() {
        return Arrays.asList(manager);
    }
}
