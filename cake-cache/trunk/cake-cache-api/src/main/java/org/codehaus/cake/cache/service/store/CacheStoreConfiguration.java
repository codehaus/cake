package org.codehaus.cake.cache.service.store;

import java.io.File;
import java.io.IOException;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.store.jdbm.JdbmCacheStore;
import org.codehaus.cake.cache.service.store.jdbm.JdbmCacheStoreConfiguration;

public class CacheStoreConfiguration<K, V> {

    private Object store;

    /**
     * Sets the cache store as a temporary Useful for testing.
     * <p>
     * Currently this method will create temporary a single file using {@link File#createTempFile(String, String)} to
     * store elements. When the virtual machine terminates the file will be deleted ({@link File#deleteOnExit()}).
     * However, the format may change in subsequent releases.
     * 
     * @return
     */
    public CacheStoreConfiguration<K, V> setStoreTemporary() {
        JdbmCacheStoreConfiguration conf = new JdbmCacheStoreConfiguration();
        try {
            File f = File.createTempFile(Cache.class.getPackage().getName(), ".tmpcache");
            f.deleteOnExit();
            conf.setLocation(f);
        } catch (IOException e) {
            throw new RuntimeException("could not create temporary file", e);
        }
        store = new JdbmCacheStore(conf);
        return this;
    }

    public static void main(String[] args) throws InterruptedException {
        new CacheStoreConfiguration().setStoreTemporary();
        Thread.sleep(20000);
    }

    public CacheStoreConfiguration<K, V> setStore(SimpleCacheStore<K, V> store) {
        this.store = store;
        return this;
    }

    public Object getStore() {
        return store;
    }
}
