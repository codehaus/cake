package org.codehaus.cake.cache.test.tck.crud;

import static org.codehaus.cake.test.util.TestUtil.params;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.codehaus.cake.cache.CacheBatchWriter;
import org.codehaus.cake.test.util.CollectionTestUtil;
import org.codehaus.cake.util.attribute.Attributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WriterBatchNPE {

    CacheBatchWriter<Integer, String, ?> writer;

    public WriterBatchNPE(CrudSuite.CrudWriterExtractors extractor) {
        this.writer = extractor.bothBatch().getSecond();
        assertNotNull(writer);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return params(1, CrudSuite.CrudWriterExtractors.values());
    }

    @Test(expected = NullPointerException.class)
    public void put() {
        writer.putAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void putKeyNull() {
        writer.putAll(CollectionTestUtil.mapWithKeyNull());
    }

    @Test(expected = NullPointerException.class)
    public void putValueNull() {
        writer.putAll(CollectionTestUtil.mapWithValueNull());
    }

    @Test(expected = NullPointerException.class)
    public void putAMap() {
        writer.putAll(null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putAAttributes() {
        writer.putAll(CollectionTestUtil.asMap(1, "A"), null);
    }

    @Test(expected = NullPointerException.class)
    public void putAKeyNull() {
        writer.putAll(CollectionTestUtil.mapWithKeyNull(), Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void putAValueNull() {
        writer.putAll(CollectionTestUtil.mapWithValueNull(), Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void removeAll() {
        writer.removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllIn() {
        writer.removeAll(CollectionTestUtil.listWithNull());
    }

}
