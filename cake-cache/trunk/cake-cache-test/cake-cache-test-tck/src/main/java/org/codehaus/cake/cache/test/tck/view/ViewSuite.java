package org.codehaus.cake.cache.test.tck.view;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { CacheViewTest.class, ViewNpe.class })
public class ViewSuite {

}
