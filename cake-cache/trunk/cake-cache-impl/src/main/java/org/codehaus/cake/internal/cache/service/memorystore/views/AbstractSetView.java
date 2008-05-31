package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.cake.internal.cache.InternalCache;

abstract class AbstractSetView<E> extends AbstractCollectionView<E> implements Set<E> {

    AbstractSetView(InternalCache cache) {
        super(cache);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Set))
            return false;
        Collection c = (Collection) o;
        if (c.size() != size())
            return false;
        try {
            return containsAll(c);
        }/* catch (ClassCastException unused) {
            return false; //not currently used
        }*/ catch (NullPointerException unused) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        Iterator<E> i = iterator();
        while (i.hasNext()) {
            E obj = i.next();
            // values, keys are never null
            h += obj.hashCode();
        }
        return h;
    }

}
