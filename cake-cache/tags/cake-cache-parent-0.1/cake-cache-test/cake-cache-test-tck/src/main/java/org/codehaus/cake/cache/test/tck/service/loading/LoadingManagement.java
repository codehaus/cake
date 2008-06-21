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
package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.cache.service.loading.CacheLoadingMXBean;
import org.codehaus.cake.cache.test.tck.service.management.AbstractManagementTest;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Test;

@RequireService( { Manageable.class })
public class LoadingManagement extends AbstractManagementTest {
    CacheLoadingMXBean mxBean() {
        return getManagedInstance(CacheLoadingMXBean.class);
    }

    @Test
    public void withLoadAll() {
        mxBean().loadAll();
        assertSize(0);
        mxBean().forceLoadAll();
        assertSize(0);
    }

    @Test
    public void withLoadForce() {
        put(M1);
        put(M2);
        mxBean().loadAll();
        mxBean().loadAll();
        assertLoadCount(0);
        loader.withLoader(M1).setValue("3");
        mxBean().forceLoadAll();
        awaitFinishedThreads();
        assertLoadCount(2);
        assertPeek(entry(M1, "3"));
        mxBean().forceLoadAll();
        awaitFinishedThreads();
        assertLoadCount(4);

    }
}
