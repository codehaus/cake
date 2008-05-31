package org.codehaus.cake.cache.test.tck;

import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.test.tck.attributes.AttributeSuite;
import org.codehaus.cake.cache.test.tck.core.CoreSuite;
import org.codehaus.cake.cache.test.tck.lifecycle.LifecycleSuite;
import org.codehaus.cake.cache.test.tck.service.exceptionhandling.ExceptionHandlingSuite;
import org.codehaus.cake.cache.test.tck.service.loading.LoadingSuite;
import org.codehaus.cake.cache.test.tck.service.management.ManagementSuite;
import org.codehaus.cake.cache.test.tck.service.memorystore.SuiteMemoryStore;
import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.codehaus.cake.service.test.tck.TckRunner;
import org.junit.internal.runners.InitializationError;

public class CacheTckRunner extends TckRunner {

    public CacheTckRunner(Class<?> clazz) throws InitializationError {
        super(clazz, CacheConfiguration.class);
    }

    protected void initialize() throws Exception {
        super.initialize();

        add(new ServiceSuite(AttributeSuite.class));
        add(new ServiceSuite(ExceptionHandlingSuite.class));
        
        add(new ServiceSuite(ManagementSuite.class));
        add(new ServiceSuite(CoreSuite.class));
        add(new ServiceSuite(LifecycleSuite.class));
        // services
        add(new ServiceSuite(SuiteMemoryStore.class));
        add(new ServiceSuite(LoadingSuite.class));
    }
}
