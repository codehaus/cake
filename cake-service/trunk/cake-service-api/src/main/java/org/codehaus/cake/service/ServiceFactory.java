package org.codehaus.cake.service;

import org.codehaus.cake.attribute.AttributeMap;

public interface ServiceFactory<T> {

    T lookup(AttributeMap attributes);
    // hasServiceWithAttributes(AttributeMap attributes);
}
