package org.codehaus.cake.internal.cache.memorystore.openadressing;

import java.io.Serializable;
import java.util.Comparator;

public class OpenAdressingMemoryStoreSupport {

    /**
     * A Comparator for Comparable.objects using their <i>natural ordering</i>. The comparator is Serializable.
     */
    public static final Comparator NATURAL_VALUE_COMPARATOR = new NaturalValueComparator();

    public static final Comparator NATURAL_VALUE_REVERSE_COMPARATOR = new NaturalValueReverseComparator();

    /** A Comparator for Comparable.objects. */
    static final class NaturalValueComparator<T extends Comparable<? super T>> implements
            Comparator<OpenAdressingEntry<Object, T>>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public int compare(OpenAdressingEntry<Object, T> a, OpenAdressingEntry<Object, T> b) {
            return a.value.compareTo(b.value);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return NATURAL_VALUE_COMPARATOR;
        }
    }

    /** A Comparator for Comparable.objects. */
    static final class NaturalValueReverseComparator<T extends Comparable<? super T>> implements
            Comparator<OpenAdressingEntry<Object, T>>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public int compare(OpenAdressingEntry<Object, T> a, OpenAdressingEntry<Object, T> b) {
            return b.value.compareTo(a.value);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return NATURAL_VALUE_REVERSE_COMPARATOR;
        }
    }
}
