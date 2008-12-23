package org.codehaus.cake.internal.service.configuration;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.GetAttributer;

/**
 * Settings, options, configurations
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public interface ConfigurationService /*WithAttribute*/ {
    <T> void update(Attribute<T> attribute, T value);

    void updateAll(AttributeMap attributes);

    GetAttributer getAttributes();
    // Compare and Set update
    //<T> void update(Attribute<T> attribute, T value, T previousValue);

}
