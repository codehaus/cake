package org.codehaus.cake.service.test.tck.core;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { DebugTrace.class, Name.class, Services.class })
public class CoreSuite {}
