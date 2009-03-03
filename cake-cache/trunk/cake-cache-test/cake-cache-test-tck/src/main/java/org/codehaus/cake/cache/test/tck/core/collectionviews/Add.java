package org.codehaus.cake.cache.test.tck.core.collectionviews;

import static org.codehaus.cake.cache.test.tck.core.collectionviews.Generators.ENTRYSET_GENERATOR;
import static org.codehaus.cake.cache.test.tck.core.collectionviews.Generators.VALUE_GENERATOR;
import static org.codehaus.cake.test.util.TestUtil.params;

import java.util.Collection;

import org.codehaus.cake.test.util.CollectionTestUtil;
import org.codehaus.cake.util.ops.Ops.Generator;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class Add extends CollectionNoAdd {

    public Add(Generator<Collection> factory, Object validElement) {
        super(factory, validElement);
    }

    @Parameters
    public static Collection<?> data() {
        return params(2, 1, ENTRYSET_GENERATOR, CollectionTestUtil.M1, VALUE_GENERATOR, "C");
    }
}
