package org.codehaus.cake.cache.test.tck.core.collectionviews;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.codehaus.cake.ops.Ops.Generator;
import org.junit.Test;

/**
 * Tests a collection where the {@link Collection#add(Object)} method is not supported.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EntrySetAdd.java 511 2007-12-13 14:37:02Z kasper $
 */
public class CollectionNoAdd<T> {
    private final Collection<T> collection;

    private final T validElement;

    public CollectionNoAdd(Generator<Collection<T>> factory, T validElement) {
        this.collection = factory.op();
        this.validElement = validElement;
    }

    /**
     * Tests that {@link Set#addAll(java.util.Collection)} throws a {@link NullPointerException} when invoked with
     * <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void addAllNPE() {
        try {
            collection.addAll(null);
        } catch (UnsupportedOperationException e) {
            throw new NullPointerException(); // ok
        }
    }

    /**
     * Tests that {@link Set#addAll(java.util.Collection)} throws a {@link UnsupportedOperationException} when invoked
     * with a valid element.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addAllUOE() {
        collection.addAll(Arrays.asList(validElement, validElement));
    }

    /**
     * Tests that {@link Set#add(Object)} throws a {@link NullPointerException} when invoked with <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void addNPE() {
        try {
            collection.add(null);
        } catch (UnsupportedOperationException e) {
            throw new NullPointerException(); // OK
        }
    }

    /**
     * Tests that {@link Set#add(Object)} throws a {@link UnsupportedOperationException} when invoked with a valid
     * element.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addUOE() {
        collection.add(validElement);
    }
}
