package org.codehaus.cake.cache.attribute;

import org.codehaus.cake.attribute.AttributeMap;

public interface AttributeBuilder<T> {

    /**
     * If an attribute is secret the presense is not in calls to {@link AttributeMap#attributeSet()},
     * {@link AttributeMap#contains(org.codehaus.cake.attribute.Attribute)} ,
     * {@link AttributeMap#entrySet()}, {@link AttributeMap#hashCode()}. The only is through calls
     * to {@link AttributeMap#get(org.codehaus.cake.attribute.Attribute)},
     * {@link AttributeMap#put(org.codehaus.cake.attribute.Attribute, Object)},
     * {@link AttributeMap#remove(org.codehaus.cake.attribute.Attribute)} or one their primitive
     * counterparts.
     * 
     */
    void isSecret();
}
