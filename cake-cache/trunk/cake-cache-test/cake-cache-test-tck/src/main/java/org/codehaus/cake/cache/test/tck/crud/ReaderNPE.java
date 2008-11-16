package org.codehaus.cake.cache.test.tck.crud;

import static org.codehaus.cake.test.util.TestUtil.params;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.service.crud.CrudReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ReaderNPE {
    CrudReader<Integer, ?> reader;

    public ReaderNPE(CrudSuite.CrudReadExtractors extractor) {
        this.reader = extractor.create();
        assertNotNull(reader);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return params(1, CrudSuite.CrudReadExtractors.values());
    }

    @Test(expected = NullPointerException.class)
    public void getNPE() {
        reader.get(null);
    }

    @Test(expected = NullPointerException.class)
    public void getKeyNPE() {
        reader.get(null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void getAttributesNPE() {
        reader.get(null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void getAllNPE() {
        reader.getAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void getAll1NPE() {
        reader.getAll(Arrays.asList(1, 2, null, 4));
    }

    // Should not fail
    @Test
    public void getAllShouldNotFail() {
        reader.getAll(Arrays.asList(1, 2, 3, 4));
    }
}
