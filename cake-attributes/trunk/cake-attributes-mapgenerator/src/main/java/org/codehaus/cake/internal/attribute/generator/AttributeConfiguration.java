package org.codehaus.cake.internal.attribute.generator;

import org.codehaus.cake.attribute.Attribute;

public interface AttributeConfiguration {
    Attribute getAttribute();
    boolean isPrivate();
    boolean isFinal();
    boolean allowGet();
    boolean allowPut();
}
