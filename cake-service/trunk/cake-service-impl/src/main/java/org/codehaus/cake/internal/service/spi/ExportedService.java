package org.codehaus.cake.internal.service.spi;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.util.attribute.AttributeMap;

public interface ExportedService {
    Object lookup(Class<?> key, AttributeMap attributes);
    boolean exportTo(Container container);
}
