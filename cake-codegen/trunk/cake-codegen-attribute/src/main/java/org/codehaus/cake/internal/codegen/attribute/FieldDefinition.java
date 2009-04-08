package org.codehaus.cake.internal.codegen.attribute;

import org.codehaus.cake.internal.codegen.Visibility;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public class FieldDefinition<T extends FieldDefinition<?>> {
    private boolean isFinal;
    private boolean isVolatile;
    private final String name;
    private final Class<?> type;
    private Visibility visibility = Visibility.PACKAGE;
    private final MutableAttributeMap context = new DefaultAttributeMap();

    public Object getInitialValue() {
        return initialValue;
    }

    private Object initialValue;
    private Attribute<?> attribute;

    public FieldDefinition(Class<?> type, String name) {
        if (type == null) {
            throw new NullPointerException("type is null");
        } else if (name == null) {
            throw new NullPointerException("name is null");
        } else if (name.equals("")) {
            name = "Noname" + System.nanoTime();
        }
        this.type = type;
        this.name = name;
    }

    public Attribute<?> getAttribute() {
        return attribute;
    }

    public T setAttribute(Attribute<?> attribute) {
        this.attribute = attribute;
        return (T) this;
    }

    public MutableAttributeMap getContext() {
        return context;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isVolatile() {
        return isVolatile;
    }

    public T setInitialValue(Object initialValue) {
        if (!(initialValue instanceof Number || initialValue instanceof String || initialValue instanceof Boolean)) {
            throw new IllegalArgumentException();
        }
        this.initialValue = initialValue; // should only be boxed primitives, or strings
        return (T) this;
    }

    public T setFinal(boolean isFinal) {
        this.isFinal = isFinal;
        return (T) this;
    }

    public T setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return (T) this;
    }

    public T setVolatile(boolean isVolatile) {
        this.isVolatile = isVolatile;
        return (T) this;
    }
}
