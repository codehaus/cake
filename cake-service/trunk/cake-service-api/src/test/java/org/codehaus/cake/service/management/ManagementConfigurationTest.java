/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.service.management;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.codehaus.cake.management.ManagedVisitor;
import org.codehaus.cake.service.common.management.ManagementConfiguration;
import org.junit.Before;
import org.junit.Test;

public class ManagementConfigurationTest {

    ManagementConfiguration m;

    @Before
    public void setUp() {
        m = new ManagementConfiguration();
    }

    @Test
    public void domain() {
        assertNull(m.getDomain());
        assertSame(m, m.setDomain("mydomain"));
        assertEquals("mydomain", m.getDomain());
        assertSame(m, m.setDomain(null));
        assertNull(m.getDomain());
    }

    @Test(expected = IllegalArgumentException.class)
    public void domainIAE() {
        m.setDomain("foo\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void domainIAE1() {
        m.setDomain(":");
    }

    @Test
    public void enabled() {
        assertFalse(m.isEnabled());
        assertSame(m, m.setEnabled(true));
        assertTrue(m.isEnabled());
    }

    @Test
    public void mBeanServer() {
        assertNull(m.getMBeanServer());
        MBeanServer s = MBeanServerFactory.createMBeanServer();
        assertSame(m, m.setMBeanServer(s));
        assertSame(s, m.getMBeanServer());
        MBeanServerFactory.releaseMBeanServer(s);
    }

    @Test
    public void registrant() {
        ManagedVisitor mgv = dummy(ManagedVisitor.class);
        assertNull(m.getRegistrant());
        assertSame(m, m.setRegistrant(mgv));
        assertSame(mgv, m.getRegistrant());
    }
}
