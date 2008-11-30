package org.codehaus.cake.cache;

import org.codehaus.cake.attribute.BooleanAttribute;

class CacheAttributes {

    final static BooleanAttribute READ_ONLY=new ReadOnlyAttribute();
    
    /**
     * The <tt>Hits</tt> attribute indicates the number of hits for a cache element. The mapped value must be of a
     * type <tt>long</tt> between 0 and {@link Long#MAX_VALUE}.
     */
    static final class ReadOnlyAttribute extends BooleanAttribute {
    
        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;
    
        /** Creates a new SizeAttribute. */
        ReadOnlyAttribute() {
            super("ReadOnly");
        }
    
        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheAttributes.READ_ONLY;
        }
    }
}
