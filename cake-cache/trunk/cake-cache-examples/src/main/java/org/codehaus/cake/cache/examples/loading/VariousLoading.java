/* Written by Kasper Nielsen and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
 */
package org.codehaus.cake.cache.examples.loading;

//START SNIPPET: Class
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.loading.CacheLoadingService;

public class VariousLoading {
    public static void main(String[] args) {
        Cache<String, String> cache = null;// replace with real cache instance
        CacheLoadingService<String, String> cls = cache.with().loading();

        //Forced loading will always load the specified elements
        cls.withKey("http://www.google.com").forceLoad(); //will load the specified url even if it already present in the cache
        cls.withAll().forceLoad(); // will reload all elements in the cache
        
        //(Ordinary) loading will only load the specified elements if they are either not present in the cache or expired or needs refreshing
        cls.withKey("http://www.google.com").load(); //will load the specified url only if it is not already present in the cache or if it is expired or needs refreshing 
        cls.withAll().load(); // will reload all elements in the cache that is expired or needs refreshing
    }
}
//END SNIPPET: Class
