/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.util.managed;

import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.ops.Ops.Predicate;

public class ManagedPredicate implements Manageable, Predicate {
    ManagedGroup g;

    public void manage(ManagedGroup parent) {
        g = parent;
    }

    public boolean op(Object element) {
        return false;
    }

    public ManagedGroup getManagedGroup() {
        return g;
    }
}
