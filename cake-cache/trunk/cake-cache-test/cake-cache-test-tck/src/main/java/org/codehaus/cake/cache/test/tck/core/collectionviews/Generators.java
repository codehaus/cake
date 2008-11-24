package org.codehaus.cake.cache.test.tck.core.collectionviews;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Ops.Generator;

@SuppressWarnings("unchecked")
public class Generators {
    public static final Generator<Cache<Integer, String>> CACHE_GENERATOR = new CacheGenerator();
    public static final Generator<Set> KEYSET_GENERATOR = keySetFrom((Generator) CACHE_GENERATOR);
    public static final Generator<Set> ENTRYSET_GENERATOR = entrySetFrom((Generator) CACHE_GENERATOR);
    public static final Generator<Collection> VALUE_GENERATOR = valuesFrom((Generator) CACHE_GENERATOR);

    static class CacheGenerator extends AbstractCacheTCKTest implements Generator<Cache<Integer, String>> {
        public Cache<Integer, String> op() {
            newConfiguration();
            return newCache();
        }
    }

    public static Generator<Set> keySetFrom(final Generator<Map> g) {
        return new Generator<Set>() {
            public Set op() {
                return g.op().keySet();
            }
        };
    }

    public static Generator<Set> entrySetFrom(final Generator<Map> g) {
        return new Generator<Set>() {
            public Set op() {
                return g.op().entrySet();
            }
        };
    }

    public static Generator<Collection> valuesFrom(final Generator<Map> g) {
        return new Generator<Collection>() {
            public Collection op() {
                return g.op().values();
            }
        };
    }
}
