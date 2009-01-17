package org.codehaus.cake.internal.codegen;

public class CollectionCodegen {

    public static <T extends AbstractMethod<?>> AbstractMethod<T> unmodifiableSet(AbstractMethod<T> m) {
        
        return m;
    }
}
