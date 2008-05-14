package org.codehaus.cake.service.test.tck;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Container.SupportedServices;
import org.codehaus.cake.service.executor.ExecutorsService;
import org.codehaus.cake.service.test.tck.lifecycle.LifecycleSuite;
import org.junit.internal.runners.CompositeRunner;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Sorter;

public abstract class TckRunner extends CompositeRunner {

    private final Set<Class> supportedServices;

    /**
     * If the file test-tck/src/main/resources/defaulttestclass exists. We will
     * try to open it and read which cache implementation should be tested by
     * default.
     * <p>
     * This is very usefull if you just want to run a subset of the tests in an
     * IDE.
     */

    public TckRunner(Class<?> clazz, Class<? extends ContainerConfiguration> configuration) throws InitializationError {
        super(clazz.getSimpleName() + " TCK");
        TckUtil.containerImplementation = clazz.getAnnotation(TckImplementationSpecifier.class).value();
        TckUtil.configuration = configuration;

        try {
            add(new JUnit4ClassRunner(clazz));
        } catch (InitializationError e) {
            // ignore, no valid tests in clazz
        }
        supportedServices = new HashSet(Arrays.asList(TckUtil.containerImplementation.getAnnotation(
                SupportedServices.class).value()));
        try {
            initialize();
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }
        super.sort(new Sorter(new Comparator<Description>() {
            public int compare(Description o1, Description o2) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            }
        }));
        System.out.println(isThreadSafe());
    }

    protected boolean isThreadSafe() {
        return supportedServices.contains(ExecutorsService.class);
    }

    protected void initialize() throws Exception {
        add(new ServiceSuite(LifecycleSuite.class));
        // add(new JUnit4ClassRunner(LifecycleSuite.class));
        // add(new JUnit4ClassRunner(WorkerService.class));
    }
}
