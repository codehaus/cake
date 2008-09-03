package org.codehaus.cake.service.test.tck.lifecycle;

import static org.junit.Assert.assertEquals;

import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.annotation.Startable;

public class Services {

    public static final IntAttribute I = new IntAttribute() {};

    public static class RegisterKeyNull {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(null, 0);
        }
    }

    public static class RegisterServiceNull {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, null);
        }
    }

    public static class Register0 {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 0);
        }
    }

    public static class Register1 {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1);
        }
    }

    public static class Register2 {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 2);
        }
    }

    public static class RegisterFactory0 {

        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, new ServiceFactoryStub(0));
        }
    }

    public static class RegisterFactory1 {

        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, new ServiceFactoryStub(1));
        }
    }

    public static class RegisterFactory2 {

        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, new ServiceFactoryStub(2));
        }
    }

    public static class RegisterFactoryKeyNull {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerFactory(null, new ServiceFactoryStub(1));
        }
    }

    public static class RegisterFactoryServiceNull {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, null);
        }
    }

    static class ServiceFactoryStub implements ServiceFactory<Integer> {
        private final int stride;

        ServiceFactoryStub(int stride) {
            this.stride = stride;
        }

        public Integer lookup(org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<Integer> context) {
            assertEquals(Integer.class, context.getKey());
            int val = context.getAttributes().get(I) + stride;
            return val >= 0 ? val : context.handleNext();
        }
    }
}
