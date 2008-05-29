package org.codehaus.cake.cache.test.tck.lifecycle;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.junit.Test;

public class LifecycleRegistration extends AbstractCacheTCKTest {
    @Test(expected = IllegalArgumentException.class)
    public void registerExisting() {
        conf.addService(new Register());
        newContainer();
        prestart();
    }

    public class Register {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(MemoryStoreService.class, dummy(MemoryStoreService.class));
        }
    }
}
