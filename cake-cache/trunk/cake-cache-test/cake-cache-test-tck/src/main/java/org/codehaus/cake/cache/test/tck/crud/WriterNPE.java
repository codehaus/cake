package org.codehaus.cake.cache.test.tck.crud;

import static org.codehaus.cake.test.util.TestUtil.params;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.cache.CacheWriter;
import org.codehaus.cake.cache.test.tck.crud.CrudSuite.LazyOp;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WriterNPE {

    CacheWriter<Integer, String, ?> writer;

    public WriterNPE(CrudSuite.CrudWriterExtractors extractor) {
        this.writer = extractor.create();
        assertNotNull(writer);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return params(1, CrudSuite.CrudWriterExtractors.values());
    }

    @Test(expected = NullPointerException.class)
    public void putValueNPE() {
        writer.put(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void putKeyNPE() {
        writer.put(null, "foo");
    }

    @Test(expected = NullPointerException.class)
    public void putaValueNPE() {
        writer.put(1, null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putaKeyNPE() {
        writer.put(null, "foo", Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putaAttributesNPE() {
        writer.put(1, "foo", null);
    }

    @Test(expected = NullPointerException.class)
    public void putIfPredicateNPE() {
        writer.putIf(null, 1, "2");
    }

    @Test(expected = NullPointerException.class)
    public void putIfKeyNPE() {
        writer.putIf(Predicates.TRUE, null, "2");
    }

    @Test(expected = NullPointerException.class)
    public void putIfValueNPE() {
        writer.putIf(Predicates.TRUE, 1, null);
    }

    @Test(expected = NullPointerException.class)
    public void putIfaPredicateNPE() {
        writer.putIf(null, 1, "2", Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putIfaKeyNPE() {
        writer.putIf(Predicates.TRUE, null, "2", Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putIfaValueNPE() {
        writer.putIf(Predicates.TRUE, 1, null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putIfaAttributesNPE() {
        writer.putIf(Predicates.TRUE, 1, "b", null);
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentKeyNPE() {
        writer.putIfAbsent(null, "A");
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentValueNPE() {
        writer.putIfAbsent(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentaKeyNPE() {
        writer.putIfAbsent(null, "A", Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentaValueNPE() {
        writer.putIfAbsent(1, null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentaAttributesNPE() {
        writer.putIfAbsent(1, "A", null);
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentLazyKeyNPE() {
        writer.putIfAbsentLazy(null, new LazyOp(1, "1"));
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentLazyValueNPE() {
        writer.putIfAbsentLazy(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void putIfLazyConditionNPE() {
        writer.putIfLazy(null, 1, new LazyOp(1, "1"));
    }

    @Test(expected = NullPointerException.class)
    public void putIfLazyKeyNPE() {
        writer.putIfLazy(Predicates.TRUE, null, new LazyOp(1, "1"));
    }

    @Test(expected = NullPointerException.class)
    public void putIfLazyFactoryNPE() {
        writer.putIfLazy(Predicates.TRUE, 1, null);
    }

    @Test(expected = NullPointerException.class)
    public void putIfLazyFactory1NPE() {
        writer.putIfLazy(Predicates.TRUE, 1, new Op() {
            public Object op(Object a) {
                return null;
            }
        });
    }
    @Test(expected = NullPointerException.class)
    public void putIfLazyFactory2NPE() {
        writer.putIfLazy(Predicates.TRUE, 1, new Op() {
            public Object op(Object a) {
                return new Pair(null, Attributes.EMPTY_ATTRIBUTE_MAP);
            }
        });
    }
    @Test(expected = NullPointerException.class)
    public void putIfLazyFactory3NPE() {
        writer.putIfLazy(Predicates.TRUE, 1, new Op() {
            public Object op(Object a) {
                return new Pair(2, null);

            }
        });
    }
    @Test(expected = NullPointerException.class)
    public void removeNPE() {
        writer.remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeKeyNPE() {
        writer.put(1, "A");
        writer.remove(null, "A");
    }

    @Test(expected = NullPointerException.class)
    public void removeValueNPE() {
        writer.put(1, "A");
        writer.remove(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void removeIfConditionNPE() {
        writer.put(1, "A");
        writer.removeIf(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void removeIfKeyNPE() {
        writer.put(1, "A");
        writer.removeIf(Predicates.TRUE, null);
    }

    @Test(expected = NullPointerException.class)
    public void replaceKeyNPE() {
        writer.put(1, "A");
        writer.replace(null, "A");
    }

    @Test(expected = NullPointerException.class)
    public void replaceValueNPE() {
        writer.put(1, "A");
        writer.replace(1, null);
    }

    @Test(expected = NullPointerException.class)
    public void replaceaKeyNPE() {
        writer.put(1, "A");
        writer.replace(null, "A", Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void replaceaValueNPE() {
        writer.put(1, "A");
        writer.replace(1, null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void replaceaAttributesNPE() {
        writer.put(1, "A");
        writer.replace(1, "A", (MutableAttributeMap) null);
    }

    @Test(expected = NullPointerException.class)
    public void replace3KeyNPE() {
        writer.put(1, "A");
        writer.replace(null, "A", "B");
    }

    @Test(expected = NullPointerException.class)
    public void replaceOldValueNPE() {
        writer.put(1, "A");
        writer.replace(1, null, "B");
    }

    @Test(expected = NullPointerException.class)
    public void replaceNewValueNPE() {
        writer.put(1, "A");
        writer.replace(1, "A", (String) null);
    }

    @Test(expected = NullPointerException.class)
    public void replacea3KeyNPE() {
        writer.put(1, "A");
        writer.replace(null, "A", "B", Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void replaceaOldValueNPE() {
        writer.put(1, "A");
        writer.replace(1, null, "B", Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void replaceaNewValueNPE() {
        writer.put(1, "A");
        writer.replace(1, "A", (String) null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void replacea4attributesNPE() {
        writer.put(1, "A");
        writer.replace(1, "A", "B", null);
    }
}
