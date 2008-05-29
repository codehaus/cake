package org.codehaus.cake.internal.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.JMException;

import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.internal.service.spi.ContainerInfo;
import org.codehaus.cake.internal.util.ArrayUtils;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.util.TimeFormatter;

public class LifecycleManager {
    private final List<LifecycleObject> list = new LinkedList<LifecycleObject>();
    private final InternalExceptionService ies;
    Composer composer;
    DefaultServiceManager dsm;
    ContainerInfo info;

    public LifecycleManager(InternalExceptionService ies, ContainerConfiguration conf, Composer composer) {
        this.ies = ies;
        this.composer = composer;
        dsm = composer.get(DefaultServiceManager.class);
        info = composer.get(ContainerInfo.class);
    }

    public void start(RunState state) {
        long startTime = System.nanoTime();
        if (ies.isDebugEnabled()) {
            ies.debug("Starting " + info.getContainerTypeName() + " [name=" + info.getContainerName() + ", type="
                    + composer.get(Container.class).getClass().getSimpleName() + "]");
            if (ies.isTraceEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("  ------------" + info.getContainerTypeName() + " was started by this call--------------\n");
                StackTraceElement[] trace = Thread.currentThread().getStackTrace();
                ArrayUtils.reverse(trace);
                int length = trace.length;
                int start = Math.max(0, trace.length - 12);
                for (int i = 0; i < length - 1 - start; i++) {
                    sb.append("    ");
                    sb.append("                         ".substring(0, i));
                    sb.append(trace[start + i]);
                    if (start + i < length) {
                        sb.append("\n");
                    }
                }
                sb.append("  --------------------------------------------------------");
                ies.trace(sb.toString());
            }
        }
        try {
            doStart(state);
        } catch (RuntimeException e) {
            state.trySetStartupException(e);
            runShutdown(state);
            throw e;
        } catch (Error e) {
            state.trySetStartupException(e);
            throw e;
        } finally {
            composer = null;
        }
        ies.info(info.getContainerTypeName() + " started [name = " + info.getContainerName() + ", startup time = "
                + TimeFormatter.DEFAULT.formatNanos(System.nanoTime() - startTime) + "]");
    }

    private void doStart(RunState state) {
        Set allServices = composer.prepareStart();
        for (Object o : allServices) {
            if (o != null) {
                list.add(new LifecycleObject(state, ies, o));
            }
        }
        DefaultServiceRegistrant dsr = new DefaultServiceRegistrant(composer.get(DefaultServiceManager.class));
        try {
            for (LifecycleObject lo : list) {
                lo.startRun(allServices, composer.get(ContainerConfiguration.class), dsr);
            }
        } finally {
            dsr.finished();
        }

        if (composer.hasService(DefaultManagementService.class)) {
            try {
                composer.get(DefaultManagementService.class).register(composer, allServices);
            } catch (JMException e) {
                state.trySetStartupException(e);
                throw new IllegalStateException("Could not start cache", e);
            }
        }
        state.transitionToRunning();
        /* Started */
        for (Iterator<LifecycleObject> iterator = list.iterator(); iterator.hasNext();) {
            LifecycleObject lo = iterator.next();
            lo.startedRun(composer.get(ContainerConfiguration.class), composer.get(Container.class));
            if (!lo.stopOrDisposeShouldRun()) {
                iterator.remove();
            }
        }
    }

    public void runShutdown(RunState state) {
        for (Iterator<LifecycleObject> iterator = list.iterator(); iterator.hasNext();) {
            LifecycleObject lo = iterator.next();
            lo.stopRun();
        }
    }
}
