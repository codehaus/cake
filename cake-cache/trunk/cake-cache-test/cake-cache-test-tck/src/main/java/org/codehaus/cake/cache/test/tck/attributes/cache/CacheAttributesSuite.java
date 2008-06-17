package org.codehaus.cake.cache.test.tck.attributes.cache;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A test suite consisting of all loading service test classes.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LoadingSuite.java 522 2007-12-24 11:24:35Z kasper $
 */
@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { AttributeCombinations.class, Cost.class, Hits.class, Size.class, TimeAccessed.class,
        TimeCreated.class, TimeModified.class, Version.class })
public class CacheAttributesSuite {}
