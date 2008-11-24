package org.codehaus.cake.cache.test.tck.core.collectionviews;

import java.util.Collection;
import java.util.Map;

public class AbstractCollectionViewTCKTest {

    Collection view;
    Mode mode;

    void add(Map.Entry<Integer, String> me) {
        view.add(mode.extract(me));
    }

    static enum Mode {
        ENTRYSET, KEYSET, VALUES;

        Object extract(Map.Entry<Integer, String> me) {
            if (this == ENTRYSET) {
                return me;
            } else if (this == KEYSET) {
                return me.getKey();
            } else {
                return me.getKey();
            }
        }
    }
}
