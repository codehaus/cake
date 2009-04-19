package org.codehaus.cake.internal.cache.memorystore.openadressing;

import org.codehaus.cake.internal.cache.memorystore.attribute.CachePolicyContext;
import org.codehaus.cake.internal.codegen.ClassDefiner;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.attribute.AttributeMap;

public class EnhancedOpenAdressingEntryFactory<K, V> implements OpenAdressingEntryFactory<K, V> {

    private final OpenAdressingEntryFactory<K, V> delegateTo;

    public EnhancedOpenAdressingEntryFactory(CachePolicyContext<K, V> policyContext, ClassDefiner classDefiner,
            Clock clock, InternalExceptionService<?> ies) {
        if (policyContext.getFields().size() == 0) {
            delegateTo = new NoAttributesOpenAdressingEntryFactory<K, V>();
        } else {
            delegateTo = EnhancedOpenAdressingEntryEmitter.create(classDefiner, policyContext, ies, clock);
        }
    }

    public void access(OpenAdressingEntry<K, V> entry) {
        delegateTo.access(entry);
    }

    public OpenAdressingEntry<K, V> create(K key, int hash, V value, AttributeMap params) {
        return delegateTo.create(key, hash, value, params);
    }

    public OpenAdressingEntry<K, V> update(K key, int hash, V value, AttributeMap params,
            OpenAdressingEntry<K, V> existing) {
        return delegateTo.update(key, hash, value, params, existing);
    }

    static class NoAttributesOpenAdressingEntryFactory<K, V> implements OpenAdressingEntryFactory<K, V> {

        public void access(OpenAdressingEntry<K, V> entry) {
        }

        public OpenAdressingEntry<K, V> create(K key, int hash, V value, AttributeMap params) {
            return new OpenAdressingEntry<K, V>(key, hash, value);
        }

        public OpenAdressingEntry<K, V> update(K key, int hash, V value, AttributeMap params,
                OpenAdressingEntry<K, V> existing) {
            return new OpenAdressingEntry<K, V>(key, hash, value);
        }
    }
}
