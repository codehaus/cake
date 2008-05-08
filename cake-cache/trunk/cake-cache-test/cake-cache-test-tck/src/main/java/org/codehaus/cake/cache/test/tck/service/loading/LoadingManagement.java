package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.cache.loading.CacheLoadingMXBean;
import org.codehaus.cake.cache.test.tck.service.management.AbstractManagementTest;
import org.junit.Test;

public class LoadingManagement extends AbstractManagementTest{
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
        assertLoadCount(2);
        assertPeek(entry(M1, "3"));
        mxBean().forceLoadAll();
        assertLoadCount(4);

    }
}