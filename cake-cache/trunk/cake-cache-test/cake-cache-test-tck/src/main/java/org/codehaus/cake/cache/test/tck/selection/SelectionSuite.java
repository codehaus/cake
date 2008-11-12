package org.codehaus.cake.cache.test.tck.selection;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( {IllegalSelection.class, SelectedCache.class})
public class SelectionSuite {

}
