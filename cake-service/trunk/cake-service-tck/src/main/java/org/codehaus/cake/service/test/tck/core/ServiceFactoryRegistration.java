package org.codehaus.cake.service.test.tck.core;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceProvider;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.attribute.IntAttribute;
import org.junit.Test;

public class ServiceFactoryRegistration extends AbstractTCKTest<Container, ContainerConfiguration> {
    @SuppressWarnings("serial")
    private static final Attribute<Integer> TEST_ATR = new IntAttribute() {};

    @Test
    public void serviceGet() {
        conf.addService(Integer.class, new TestFactory());
        newContainer();
        assertEquals(-1, getService(Integer.class).intValue());
        assertEquals(-1, getService(Integer.class,Attributes.EMPTY_ATTRIBUTE_MAP).intValue());
        assertEquals(Integer.MIN_VALUE, getService(Integer.class,TEST_ATR.singleton(Integer.MIN_VALUE)).intValue());
        assertEquals(5, getService(Integer.class,TEST_ATR.singleton(5)).intValue());
    }

    public static class TestFactory implements ServiceProvider<Integer> {
        public Integer lookup(org.codehaus.cake.service.ServiceProvider.Context<Integer> context) {
            if (context.getAttributes().isEmpty()) {
                return -1;
            }
            return context.getAttributes().get(TEST_ATR);
        }
    }
}
