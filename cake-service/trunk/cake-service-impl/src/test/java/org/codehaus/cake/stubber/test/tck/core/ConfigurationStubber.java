package org.codehaus.cake.stubber.test.tck.core;

import org.codehaus.cake.stubber.test.tck.AbstraktStubberTCKTst;
import org.junit.Test;

public class ConfigurationStubber extends AbstraktStubberTCKTst {
    @Test
    public void defaultSetting() {
        newContainer();
        assertEquals(10, c.with().bubber().getFooFoo());
    }
    
    @Test
    public void customSetting() {
        conf.withBubber().setFooFoo(50);
        newContainer();
        assertEquals(50, c.with().bubber().getFooFoo());
    }
}
