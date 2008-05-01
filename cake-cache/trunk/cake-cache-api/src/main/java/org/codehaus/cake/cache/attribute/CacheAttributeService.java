package org.codehaus.cake.cache.attribute;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;

public interface CacheAttributeService {

    AttributeMap getAttributes();

    <T> void setDefaultValue(Attribute<T> attribute, T defaultValue);

    <T> T getDefaultValue(Attribute<T> attribute);

    <T> void registerAttribute(Attribute<T> attribute);//attribute, defaultValue
    
    //register attributes
    //register default values, factories
    //return default values, factories, registered attributes
}
