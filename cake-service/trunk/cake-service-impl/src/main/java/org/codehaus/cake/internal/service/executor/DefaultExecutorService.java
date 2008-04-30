package org.codehaus.cake.internal.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.executor.ExecutorsManager;
import org.codehaus.cake.service.executor.ExecutorsService;

public class DefaultExecutorService implements ExecutorsService {
    private final ExecutorsManager manager;

    public DefaultExecutorService(ExecutorsConfiguration conf) {
        ExecutorsManager manager = conf.getExecutorManager();
        if (manager == null) {
            manager = new ExecutorsManager();
        }
        this.manager = manager;
    }

    public ExecutorService getExecutorService(Object service) {
        return getExecutorService(service, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public ExecutorService getExecutorService(Object service, AttributeMap attributes) {
        return manager.getExecutorService(service, attributes);
    }

    public ForkJoinExecutor getForkJoinExecutor(Object service) {
        return getForkJoinExecutor(service, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes) {
        return manager.getForkJoinExecutor(service, attributes);

    }

    public ScheduledExecutorService getScheduledExecutorService(Object service) {
        return getScheduledExecutorService(service, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public ScheduledExecutorService getScheduledExecutorService(Object service,
            AttributeMap attributes) {
        return manager.getScheduledExecutorService(service, attributes);
    }

    @Startable
    public void start(ContainerConfiguration<?> configuration, ServiceRegistrant serviceRegistrant)
            throws Exception {
        serviceRegistrant.registerService(ExecutorsService.class, this);
    }
}
