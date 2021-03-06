/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache.test.tck.service.management;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectInstance;

import org.codehaus.cake.cache.CacheMXBean;
import org.codehaus.cake.cache.loading.CacheLoadingMXBean;
import org.codehaus.cake.cache.memorystore.MemoryStoreMXBean;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractManagementTest extends AbstractCacheTCKTest {

    MBeanServer server;

    static Map<String, Class> map = new HashMap<String, Class>();
    static {
        map.put("General", CacheMXBean.class);
        map.put("Loading", CacheLoadingMXBean.class);
        map.put("MemoryStore", MemoryStoreMXBean.class);
    }

    @Before
    public void setup() {
        server = MBeanServerFactory.createMBeanServer();
        // server = ManagementFactory.getPlatformMBeanServer();
        conf.withManagement().setEnabled(true);
        conf.withManagement().setMBeanServer(server);
        super.init();
    }

    @After
    public void release() {
        MBeanServerFactory.releaseMBeanServer(server);
    }

    public AbstractCacheTCKTest init() {
        setup();
        super.init();
        return this;
    }

    protected <T> T getManagedInstance(Class<T> ofType) {
        if (ofType == null) {
            throw new NullPointerException("type is null");
        } else if (!map.containsValue(ofType)) {
            throw new NullPointerException("type is unknown + " + ofType);
        }
        size();// prestart

        Set<ObjectInstance> s = server.queryMBeans(null, null);
        for (ObjectInstance oi : s) {
            // System.out.println(oi.getClassName());
            if (map.containsKey(oi.getClassName()) && map.get(oi.getClassName()).equals(ofType)) {
                return (T) MBeanServerInvocationHandler.newProxyInstance(server, oi.getObjectName(), ofType, false);

            }
        }
        throw new IllegalArgumentException("Did not find MXBean of type " + ofType + ", [registered = " + s + "]");
    }
}
