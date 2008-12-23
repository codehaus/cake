package org.codehaus.cake.cache.policy;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.GetAttributer;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.Caches;
import org.codehaus.cake.cache.policy.AbstractCakeReplacementPolicy.Reg;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;
import org.junit.Before;

public abstract class AbstractPolicyTest {

    protected CacheEntry<Integer, String> TE1;
    protected CacheEntry<Integer, String> TE2;
    protected CacheEntry<Integer, String> TE3;
    protected CacheEntry<Integer, String> TE4;
    protected CacheEntry<Integer, String> TE5;

    private IdentityHashMap<GetAttributer, AttributeMap> a;

    protected AbstractCakeReplacementPolicy<Integer, String> policy;

    private Map<Integer, CacheEntry<Integer, String>> values;

    @Before
    public void cleanup() {
        a = new IdentityHashMap<GetAttributer, AttributeMap>();
        values = new HashMap<Integer, CacheEntry<Integer, String>>();
        TE1 = createEntry();
        TE2 = createEntry();
        TE3 = createEntry();
        TE4 = createEntry();
        TE5 = createEntry();
    }

    public void addToPolicy(int start, int stop) {
        for (int i = start; i <= stop; i++) {
            policy.add(createEntry(i, "" + i));
        }
    }

    public List<Integer> empty() {
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (;;) {
            CacheEntry<Integer, String> ce = policy.evictNext();
            if (ce != null)
                list.add(ce.getKey());
            else
                break;
        }
        return list;
    }

    protected void init() {
        policy.registerAttribute(new DD());
    }

    public CacheEntry<Integer, String> createEntry() {
        DefaultAttributeMap am = new DefaultAttributeMap();
        CacheEntry<Integer, String> ce = Caches.newEntry(1, "v", am);
        a.put(ce, am);
        return ce;
    }

    public void add(int key) {
        policy.add(createEntry(key, "" + key));
    }

    public CacheEntry<Integer, String> createEntry(Integer key, String value) {
        DefaultAttributeMap am = new DefaultAttributeMap();
        CacheEntry<Integer, String> ce = Caches.newEntry(key, value, am);
        a.put(ce, am);
        values.put(ce.getKey(), ce);
        return ce;
    }

    public CacheEntry<Integer, String> val(int key) {
        return values.get(key);
    }

    public <T> CacheEntry<Integer, String> createEntry(Attribute<T> atr, T value) {
        DefaultAttributeMap am = new DefaultAttributeMap();
        am.put(atr, value);
        CacheEntry<Integer, String> ce = Caches.newEntry(new Long(System.nanoTime()).hashCode(), "v", am);
        a.put(ce, am);
        return ce;
    }

    public Integer[] evict(int num) {
        if (num > 1000)
            throw new IllegalArgumentException("must be <1000");
        Integer[] result = new Integer[num];
        for (int i = 0; i < result.length; i++) {
            result[i] = policy.evictNext().getKey();
        }
        return result;
    }

    public <T> void set(CacheEntry<?, ?> entry, Attribute<T> a, T value) {
        get(entry).put(a, value);
    }

    public AttributeMap get(GetAttributer ce) {
        return a.get(ce);
    }

    class DD implements InternalCacheAttributeService {

        public void attachToPolicy(Attribute<?> attribute) {
        }

        public void dependOnHard(Attribute<?> attribute) {
        }

        public void dependOnSoft(Attribute<?> attribute) {
        }

        public Reg<?> newBoolean() {
            return newObject(Boolean.TYPE);
        }

        public Reg<?> newInt() {
            return newObject(Integer.TYPE);
        }

        public <T> Reg<T> newObject(Class<T> type) {
            if (type.equals(Boolean.TYPE)) {
                ObjectAttribute<T> oa = new ObjectAttribute<T>(type) {};
                if (true) {
                    throw new UnsupportedOperationException();
                }
                return new ObjectRef<T>(oa);
            } else if (type.equals(Integer.TYPE)) {
                IntAttribute a = new IntAttribute() {};
                return new IntegerRef(a);
            } else {
                ObjectAttribute<T> oa = new ObjectAttribute<T>(type) {};
                return new ObjectRef<T>(oa);
            }

        }
    }

    class ObjectRef<T> extends AbstractRef<T> {
        ObjectAttribute<T> a;

        ObjectRef(ObjectAttribute<T> a) {
            this.a = a;
        }

        @Override
        public T getObject(GetAttributer entry) {
            return get(entry).get(a);
        }

        @Override
        public void setObject(GetAttributer entry, T value) {
            get(entry).put(a, value);
        }
    }

    class IntegerRef<T> extends AbstractRef<T> {
        IntAttribute a;

        IntegerRef(IntAttribute a) {
            this.a = a;
        }

        @Override
        public int getInt(GetAttributer entry) {
            return get(entry).get(a);
        }

        @Override
        public void setInt(GetAttributer entry, int value) {
            get(entry).put(a, value);
        }
    }

    static abstract class AbstractRef<T> implements Reg<T> {
        public int getInt(GetAttributer entry) {
            throw new UnsupportedOperationException();
        }

        public void setInt(GetAttributer entry, int value) {
            throw new UnsupportedOperationException();
        }

        public boolean getBoolean(GetAttributer entry) {
            throw new UnsupportedOperationException();
        }

        public T getObject(GetAttributer entry) {
            throw new UnsupportedOperationException();
        }

        public void setBoolean(GetAttributer entry, boolean value) {
            throw new UnsupportedOperationException();
        }

        public void setObject(GetAttributer entry, T value) {
            throw new UnsupportedOperationException();
        }
    }
}
