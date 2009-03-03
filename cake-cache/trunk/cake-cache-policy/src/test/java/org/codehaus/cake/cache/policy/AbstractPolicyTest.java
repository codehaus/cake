package org.codehaus.cake.cache.policy;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public abstract class AbstractPolicyTest {

    protected ReplacementPolicy<Integer> policy;

    public void addToPolicy(int start, int stop) {
        for (int i = start; i <= stop; i++) {
            policy.add(i);
        }
    }

    public List<Integer> empty() {
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (;;) {
            Integer ce = policy.evictNext();
            if (ce != null)
                list.add(ce);
            else
                break;
        }
        return list;
    }

    public <T> AttributeMap createEntry(Attribute<T> atr, T value) {
        DefaultAttributeMap am = new DefaultAttributeMap();
        am.put(atr, value);
        return am;
    }

    public Integer[] evict(int num) {
        if (num > 1000)
            throw new IllegalArgumentException("must be <1000");
        Integer[] result = new Integer[num];
        for (int i = 0; i < result.length; i++) {
            result[i] = policy.evictNext();
        }
        return result;
    }

    public <T> void set(AttributeMap entry, Attribute<T> a, T value) {
       ((MutableAttributeMap) entry).put(a, value);
    }

}
