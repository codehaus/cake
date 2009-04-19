/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.attributes;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.loading.CacheLoader;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public class CostDecoratedLoader {
  

    @CacheLoader
    public String load(String key, MutableAttributeMap attributes) throws Exception {
        long start = System.nanoTime();
        //DO THE ACTUAL LOADING
        String result=key.toLowerCase();
        attributes.put(CacheEntry.COST, System.nanoTime() - start);
        return result;
    }
}
