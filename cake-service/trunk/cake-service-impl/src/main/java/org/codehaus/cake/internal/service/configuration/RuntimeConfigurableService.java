package org.codehaus.cake.internal.service.configuration;

import java.util.Set;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;

public interface RuntimeConfigurableService {
    Set<Attribute<?>> getRuntimeConfigurableAttributes();

    void updateConfiguration(AttributeMap attributes);
    
}
