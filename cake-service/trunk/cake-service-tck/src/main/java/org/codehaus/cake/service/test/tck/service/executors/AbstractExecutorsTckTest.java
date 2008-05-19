package org.codehaus.cake.service.test.tck.service.executors;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;

public class AbstractExecutorsTckTest extends AbstractTCKTest<Container, ContainerConfiguration> {

    void doFail(String string) {
        // TODO Auto-generated method stub
        System.err.println(string);
    }
}
