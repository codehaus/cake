/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.service.test.tck.service.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.executor.ExecutorsManager;
import org.codehaus.cake.service.executor.ExecutorsService;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.service.test.tck.UnsupportedServices;
import org.junit.Test;

@UnsupportedServices(value = { ExecutorsService.class })
public class ExecutorsNoSupport extends AbstractTCKTest<Container, ContainerConfiguration> {
    @Test(expected = IllegalArgumentException.class)
    public void noExecutorsSupport() throws Throwable {
        withConf(ExecutorsConfiguration.class).setExecutorManager(new ExecutorsManager() {
            public ExecutorService getExecutorService(Object service, AttributeMap attributes) {
                return null;
            }

            public ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes) {
                return null;
            }

            public ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes) {
                return null;
            }
        });
        cheatInstantiate();
    }
}
