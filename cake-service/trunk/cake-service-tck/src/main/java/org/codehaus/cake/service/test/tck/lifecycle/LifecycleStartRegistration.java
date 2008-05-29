package org.codehaus.cake.service.test.tck.lifecycle;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

public class LifecycleStartRegistration extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test(expected = UnsupportedOperationException.class)
    public void registerNone() {
        newContainer();
        c.getService(Integer.class).intValue();
    }

    @Test
    public void register() {
        conf.addService(new Register());
        newContainer();
        prestart();
        assertEquals(1000, c.getService(Integer.class).intValue());
    }

    @Test(expected = NullPointerException.class)
    public void registerNullKey() {
        conf.addService(new RegisterNullKey());
        newContainer();
        prestart();
    }

    @Test(expected = NullPointerException.class)
    public void registerNullService() {
        conf.addService(new RegisterNullService());
        newContainer();
        prestart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerSame() {
        conf.addService(new RegisterSame());
        newContainer();
        prestart();
    }
    @Test(expected = IllegalArgumentException.class)
    public void registerSameTwice() {
        conf.addService(new Register());
        conf.addService(new Register());
        newContainer();
        prestart();
    }
    public static class Register {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1000);
        }
    }

    public static class RegisterNullKey {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(null, 1000);
        }
    }

    public static class RegisterNullService {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, null);
        }
    }

    public static class RegisterSame {
        @Startable
        public void start1(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1000);
        }

        @Startable
        public void start2(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1000);
        }
    }
}
