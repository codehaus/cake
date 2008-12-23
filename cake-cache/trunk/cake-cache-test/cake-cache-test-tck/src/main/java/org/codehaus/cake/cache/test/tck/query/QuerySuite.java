package org.codehaus.cake.cache.test.tck.query;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { OrderedQuery.class, QueryNpe.class, SimpleQuery.class })
public class QuerySuite {

}
