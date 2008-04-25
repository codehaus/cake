/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.attributes;

import org.codehaus.cake.attribute.ObjectAttribute;


public class StringAttribute2 extends ObjectAttribute<String> {

    public static final StringAttribute2 INSTANCE = new StringAttribute2();

    /** serialVersionUID. */
    private static final long serialVersionUID = 1821856356464961171L;

    private StringAttribute2() {
        super("StringAttribute2", String.class, "string2");
    }

}
