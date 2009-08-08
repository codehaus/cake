package org.codehaus.cake.internal.service.spi;

import org.codehaus.cake.service.Container;

public interface ContainerComposer {
    Class<? extends Container> getContainerType();

    String getContainerName();

    void register(Object service, Class<?> exportAs);

    void initialize();
}
