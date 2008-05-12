package org.codehaus.cake.cache.attribute;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.CacheEntry;

public class CacheAttributeConfiguration {

    private List attributes = new ArrayList();

    public List getAllAttributes() {
        return new ArrayList(attributes);
    }

    public CacheAttributeConfiguration add(Attribute... a) {
        for (Attribute aa : a) {
            if (attributes.contains(aa)) {
                throw new IllegalArgumentException("Attribute has already been added [Attribute ="
                        + aa + "");
            }
        }
        for (Attribute aa : a) {
            attributes.add(aa);
        }
        return this;
    }

    public static void main(String[] args) {
        CacheEntry ce = null;
        CacheEntry.COST.get(ce);
    }
}
