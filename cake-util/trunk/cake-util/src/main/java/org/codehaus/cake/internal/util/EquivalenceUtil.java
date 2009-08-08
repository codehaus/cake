package org.codehaus.cake.internal.util;

import java.io.Serializable;

import org.codehaus.cake.util.Equivalence;

public class EquivalenceUtil {
    
    /**
     * An Equivalence object performing {@link Object#equals} based comparisons and using {@link Object#hashCode}
     * hashing
     */
    public static final Equivalence<Object> EQUALS = new EquivalenceUsingEquals();

    /**
     * An Equivalence object performing identity-based comparisons and using {@link System#identityHashCode} for hashing
     */
    public static final Equivalence<Object> IDENTITY = new EquivalenceUsingIdentity();

    static final class EquivalenceUsingEquals implements Equivalence<Object>, Serializable {
        private static final long serialVersionUID = 1L;

        public final boolean equal(Object a, Object b) {
            return a.equals(b);
        }

        public final int hash(Object a) {
            return a.hashCode();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return EQUALS;
        }
    }

    static final class EquivalenceUsingIdentity implements Equivalence<Object>, Serializable {
        private static final long serialVersionUID = 1L;

        public final boolean equal(Object a, Object b) {
            return a == b;
        }

        public final int hash(Object a) {
            return System.identityHashCode(a);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return IDENTITY;
        }
    }

}
