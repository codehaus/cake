package org.codehaus.cake.internal.codegen.attribute;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.BooleanAttribute;

public class SingleElementDecorated extends SingleElement {

    public final static Set keyset_ATR;

    // access flags 25
    public final static BooleanAttribute ba_ATR;

    private final boolean ba;

    public SingleElementDecorated(boolean ba) {
        this.ba = ba;
    }

    public SingleElementDecorated(Object o, boolean ba) {
        super(o);
        this.ba = ba;
    }

    static {
        Attribute[] a = AttributeDecorator.remove(SingleElementDecorated.class);
        keyset_ATR = new HashSet(Arrays.asList(a));
        ba_ATR = (BooleanAttribute) a[0];
    }
}
