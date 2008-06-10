package org.codehaus.cake.internal.attribute.generator;

import org.codehaus.cake.attribute.Attribute;

public class DefaultAttributeConfiguration implements AttributeConfiguration {
    private final Attribute a;

    private boolean allowGet;
    private boolean allowPut;
    private boolean isPrivate;
    private boolean isFinal;

    public DefaultAttributeConfiguration(Attribute a, boolean isMutable, boolean isHidden) {
        this(a, !isHidden, !isHidden, !isMutable, true);
    }

    public DefaultAttributeConfiguration(Attribute a, boolean allowGet, boolean allowPut, boolean isFinal,
            boolean isPrivate) {
        if (a == null) {
            throw new NullPointerException("a is null");
        }
        this.a = a;
        this.allowGet = allowGet;
        this.allowPut = allowPut;
        this.isFinal = isFinal;
        this.isPrivate = isPrivate;
    }

    public DefaultAttributeConfiguration(DefaultAttributeConfiguration other) {
        this(other.a, other.allowGet, other.allowPut, other.isFinal, other.isPrivate);
    }

    @Override
    public boolean equals(Object obj) {
        DefaultAttributeConfiguration c = (DefaultAttributeConfiguration) obj;
        return c.a == a && c.allowGet == allowGet && c.allowPut == allowPut && c.isFinal == isFinal
                && c.isPrivate == isPrivate;
    }

    public Attribute getAttribute() {
        return a;
    }

    @Override
    public int hashCode() {
        return a.hashCode();
    }

    public boolean allowGet() {
        return allowGet;
    }

    public String toString() {
        return a + ", allowGet=" + allowGet + ", allowPut=" + allowPut + ", isPrivate=" + isPrivate + ", isFinal="
                + isFinal;
    }

    public boolean allowPut() {
        return allowPut;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isFinal() {
        return !isFinal;
    }
}
