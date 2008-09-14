package org.codehaus.cake.cache.service.crud;

import java.util.concurrent.ConcurrentMap;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.Caches;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.ContainerAlreadyShutdownException;

/**
 * This service can be used to create, update, or delete entries from the cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java 520 2007-12-21 17:53:31Z kasper $
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 * @param <R>
 *            the type returned from methods
 */
public interface WriteService<K, V, R> extends UpdateService<K, V, R>, PutService<K, V, R>, CreateService<K, V, R>,
        DeleteService<K, V, R> {

    /**
     * 
     * Used for transforming CacheEntry to R
     */
    ObjectAttribute<Op<?, ?>> WRITE_TRANSFORMER = (ObjectAttribute) new ObjectAttribute<Op>(Op.class) {};

}
