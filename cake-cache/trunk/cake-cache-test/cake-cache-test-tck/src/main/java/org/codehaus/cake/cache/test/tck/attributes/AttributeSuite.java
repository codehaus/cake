package org.codehaus.cake.cache.test.tck.attributes;

import org.codehaus.cake.cache.test.tck.attributes.cache.CacheAttributesSuite;
import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { CacheAttributesSuite.class})
public class AttributeSuite {}


