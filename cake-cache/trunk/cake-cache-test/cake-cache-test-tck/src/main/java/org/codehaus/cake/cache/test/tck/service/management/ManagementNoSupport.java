/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.management;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class ManagementNoSupport extends AbstractCacheTCKTest {
    @Test(expected = IllegalArgumentException.class)
    public void noManagementSupport() {
        conf.withManagement().setEnabled(true);
        init();
    }
}
