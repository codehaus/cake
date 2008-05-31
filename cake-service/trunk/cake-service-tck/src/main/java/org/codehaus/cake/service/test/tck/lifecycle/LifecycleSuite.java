package org.codehaus.cake.service.test.tck.lifecycle;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { LifecycleAfterErroneous.class, LifecycleAfterStart.class, LifecycleDisposable.class,
        LifecycleDisposableErroneous.class, LifecycleStart.class, LifecycleStartErroneous.class,
        LifecycleStartRegistration.class, LifecycleStartRegistrationFactory.class, LifecycleStatus.class,
        LifecycleStoppable.class, LifecycleStoppableErroneous.class })
public class LifecycleSuite {}
