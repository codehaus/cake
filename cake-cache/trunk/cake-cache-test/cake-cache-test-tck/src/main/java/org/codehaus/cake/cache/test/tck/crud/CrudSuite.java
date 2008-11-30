package org.codehaus.cake.cache.test.tck.crud;

import static org.junit.Assert.assertEquals;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheDataExtractor;
import org.codehaus.cake.cache.service.crud.CrudBatchWriter;
import org.codehaus.cake.cache.service.crud.CrudReader;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.cache.test.tck.core.collectionviews.Generators;
import org.codehaus.cake.cache.test.util.AtrStubs;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.codehaus.cake.util.Pair;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { Reader.class, ReaderNPE.class, WriterBatchNPE.class, WriterNPE.class, WriterPutIfAbsent.class })
public class CrudSuite {
    public static enum CrudReadExtractors {
        ATTRIBUTE_INT {
            public Pair<Cache<Integer, String>, CrudReader> both() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.withCrud().attribute(AtrStubs.L_1));
            }
        },
        ATTRIBUTE_OBJECT {
            public Pair<Cache<Integer, String>, CrudReader> both() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.withCrud().attribute(AtrStubs.O_1));
            }
        },
        ENTRY {
            public Pair<Cache<Integer, String>, CrudReader> both() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.withCrud().entry());
            }
        },
        VALUE {
            public Pair<Cache<Integer, String>, CrudReader> both() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.withCrud().value());
            }
        };
        public abstract Pair<Cache<Integer, String>, CrudReader> both();

        public CrudReader<Integer, ?> create() {
            return both().getSecond();
        }
    }

    public static enum CrudWriterExtractors {
        VOID {
            public Pair<Cache<Integer, String>, CrudWriter> both() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.withCrud().write());
            }

            public Pair<Cache<Integer, String>, CrudBatchWriter> bothBatch() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.withCrud().writeBatch());
            }
        },
        ENTRY {
            public Pair<Cache<Integer, String>, CrudWriter> both() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.withCrud().writeReturnPreviousEntry());
            }

            public Pair<Cache<Integer, String>, CrudBatchWriter> bothBatch() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.getService(CrudBatchWriter.class, CrudWriter.WRITE_TRANSFORMER
                        .singleton(CacheDataExtractor.WHOLE_ENTRY)));
            }
        },
        VALUE {
            public Pair<Cache<Integer, String>, CrudWriter> both() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.withCrud().writeReturnPreviousValue());
            }

            public Pair<Cache<Integer, String>, CrudBatchWriter> bothBatch() {
                Cache<Integer, String> c = Generators.CACHE_GENERATOR.op();
                return new Pair(c, c.getService(CrudBatchWriter.class, CrudWriter.WRITE_TRANSFORMER
                        .singleton(CacheDataExtractor.ONLY_VALUE)));
            }
        };
        public abstract Pair<Cache<Integer, String>, CrudWriter> both();

        public abstract Pair<Cache<Integer, String>, CrudBatchWriter> bothBatch();

        public CrudWriter<Integer, String, ?> create() {
            return both().getSecond();
        }
    }

    static class LazyOp implements Op<Integer, Pair<String, AttributeMap>> {
        private final int key;
        private final String v;
        private final AttributeMap attributes;

        public Pair<String, AttributeMap> op(Integer a) {
            assertEquals(key, a.intValue());
            return new Pair<String, AttributeMap>(v, attributes);
        }

        public LazyOp(int key, String value) {
            this(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
        }

        public LazyOp(int key, String value, AttributeMap attributes) {
            this.key = key;
            this.v = value;
            this.attributes = attributes;
        }
    }
}
