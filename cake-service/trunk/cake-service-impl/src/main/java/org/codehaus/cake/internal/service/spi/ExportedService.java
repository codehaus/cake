package org.codehaus.cake.internal.service.spi;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.service.Container;

public interface ExportedService<T> {
    T lookup(Class<T> key, AttributeMap attributes);
    boolean exportTo(Container container);
}
