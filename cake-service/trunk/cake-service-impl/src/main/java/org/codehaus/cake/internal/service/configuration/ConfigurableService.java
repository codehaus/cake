package org.codehaus.cake.internal.service.configuration;

import org.codehaus.cake.attribute.AttributeMap;

public interface ConfigurableService {
    void processUpdate(AttributeMap attributes);
    
}
