package org.codehaus.cake.internal.cache.service.memorystore;

import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;

public class MemoryStoreAttributes {

    public static final BooleanAttribute IS_DISABLED = new IsDisabledAttribute();
    public static final IntAttribute MAX_SIZE = new MaximumSizeAttribute();
    public static final LongAttribute MAX_VOLUME = new MaximumVolumeAttribute();

    static class IsDisabledAttribute extends BooleanAttribute {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        /** @return Preserves singleton property */
        private Object readResolve() {
            return IS_DISABLED;
        }
    }

    static class MaximumSizeAttribute extends IntAttribute {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        MaximumSizeAttribute() {
            super(Integer.MAX_VALUE);
        }

        @Override
        public void checkValid(int value) {
            if (value <= 0) {
                throw new IllegalArgumentException("Size must be positive, was " + value);
            }
        }

        @Override
        public boolean isValid(int value) {
            return value >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return MAX_SIZE;
        }
    }

    static class MaximumVolumeAttribute extends LongAttribute {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        MaximumVolumeAttribute() {
            super(Long.MAX_VALUE);
        }

        @Override
        public void checkValid(long value) {
            if (value <= 0) {
                throw new IllegalArgumentException("Volume must be positive, was " + value);
            }
        }

        @Override
        public boolean isValid(long value) {
            return value >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return MAX_VOLUME;
        }
    }

}
