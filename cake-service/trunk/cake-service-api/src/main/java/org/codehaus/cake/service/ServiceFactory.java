package org.codehaus.cake.service;

import org.codehaus.cake.attribute.AttributeMap;

public interface ServiceFactory<T> {

    /**
     * @return the type of services returned by {@link #lookup()} and {@link #lookup(AttributeMap)}.
     */
    Class<T> getType();

    T lookup(AttributeMap attributes);
    // hasServiceWithAttributes(AttributeMap attributes);
}
