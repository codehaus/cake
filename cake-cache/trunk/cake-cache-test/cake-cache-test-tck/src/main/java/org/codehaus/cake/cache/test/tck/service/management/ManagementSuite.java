/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.management;

import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.test.tck.RequireService;
import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@RequireService({Manageable.class})
@Suite.SuiteClasses( { ManagementCacheMXBean.class, ManagementConfiguration.class })
public class ManagementSuite {}
