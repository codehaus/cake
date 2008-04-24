/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A test suite consisting of all loading service test classes.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LoadingSuite.java 522 2007-12-24 11:24:35Z kasper $
 */
@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { ExplicitLoadAll.class, ExplicitLoadKey.class, ExplicitLoadKeys.class,
        ExplicitNeedsReload.class, ImplicitLoading.class, LoadingLifecycle.class,
        LoadingManagement.class, LoadingNPE.class, LoadingService.class })
public class LoadingSuite {}
