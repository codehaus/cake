/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.attributes;

import org.codehaus.cake.attribute.ObjectAttribute;


public class StringAttribute1 extends ObjectAttribute<String> {

    public static final StringAttribute1 INSTANCE = new StringAttribute1();

    /** serialVersionUID. */
    private static final long serialVersionUID = 1821856356464961171L;

    private StringAttribute1() {
        super("StringAttribute1", String.class, "string1");
    }
}
