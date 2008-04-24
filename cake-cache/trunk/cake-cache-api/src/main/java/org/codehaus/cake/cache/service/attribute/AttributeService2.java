package org.codehaus.cake.cache.service.attribute;

public interface AttributeService2 {

    <T> AttributeBuilder<T> createSecret(Class<T> type);
}
