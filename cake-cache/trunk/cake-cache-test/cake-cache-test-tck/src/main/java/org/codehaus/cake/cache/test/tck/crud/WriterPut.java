package org.codehaus.cake.cache.test.tck.crud;

import static org.codehaus.cake.test.util.TestUtil.params;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheWriter;
import org.codehaus.cake.util.Pair;
import org.junit.runners.Parameterized.Parameters;

public class WriterPut {
    private Cache<Integer, String> c;
    CacheWriter<Integer, String, ?> writer;
    CrudSuite.CrudWriterExtractors extractor;
    
    public WriterPut(CrudSuite.CrudWriterExtractors extractor) {
        Pair<Cache<Integer, String>, CacheWriter> p = extractor.both();
        c = p.getFirst();
        writer = p.getSecond();
        assertNotNull(writer);
        assertNotNull(c);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return params(1,(Object[]) CrudSuite.CrudWriterExtractors.values());
    }

    public void put() {
        writer.put(1, "B");
        assertEquals(1, c.size());
        assertEquals(1, c.peekEntry(1).getKey().intValue());
        assertEquals("B", c.peekEntry(1).getValue());
        assertEquals(0, c.peekEntry(1).size());
        
        writer.put(1, "C");
        assertEquals(1, c.size());
        assertEquals(1, c.peekEntry(1).getKey().intValue());
        assertEquals("C", c.peekEntry(1).getValue());
        assertEquals(0, c.peekEntry(1).size());

        writer.put(2, "C");
        assertEquals(2, c.size());
        assertEquals(2, c.peekEntry(2).getKey().intValue());
        assertEquals("C", c.peekEntry(2).getValue());
        assertEquals(0, c.peekEntry(2).size());

    }

}
