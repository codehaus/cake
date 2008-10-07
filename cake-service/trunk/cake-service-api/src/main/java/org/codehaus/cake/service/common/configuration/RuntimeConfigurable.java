package org.codehaus.cake.service.common.configuration;

import java.util.Collection;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;

public interface RuntimeConfigurable {
    Collection<Attribute<?>> getRuntimeConfigurableAttributes();

    void updateConfiguration(AttributeMap updates);
}
