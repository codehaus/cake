package org.codehaus.cake.cache.test.tck.crud;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { Reading.class, CrudWriterPutIfAbsent.class })
public class CrudSuite {

}
