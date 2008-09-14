package org.codehaus.cake.cache.examples.loading;

// START SNIPPET: SimpleUrlLoaderUsage
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.UnsynchronizedCache;

public class SimpleUrlLoaderUsage {
    public static void main(String[] args) {
        CacheConfiguration<String, String> conf = CacheConfiguration.newConfiguration();
        conf.withLoading().setLoader(new SimpleUrlLoader());
        Cache<String, String> cache = UnsynchronizedCache.from(conf);
        System.out.println(cache.get("http://www.google.com")); // uses CacheLoader
        System.out.println(cache.get("http://www.google.com")); // uses cached version
    }
}
// END SNIPPET: SimpleUrlLoaderUsage
