package org.codehaus.cake.internal.service.configuration;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.WithAttributes;

/**
 * Settings, options, configurations
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 */
public interface ConfigurationService extends WithAttributes {
    <T> void update(Attribute<T> attribute, T value);

    void updateAll(AttributeMap attributes);

    // Compare and Set update
    //<T> void update(Attribute<T> attribute, T value, T previousValue);

}
