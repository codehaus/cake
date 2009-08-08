package org.codehaus.cake.util.attribute;

public class StringAttribute extends ObjectAttribute<String> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    protected StringAttribute() {
        super(String.class);
    }

    public StringAttribute(String defaultValue) {
        super(String.class, defaultValue);
    }

    public StringAttribute(String name, String defaultValue) {
        super(name, String.class, defaultValue);
    }

    @Override
    public String fromString(String str) {
        return str;
    }

}
