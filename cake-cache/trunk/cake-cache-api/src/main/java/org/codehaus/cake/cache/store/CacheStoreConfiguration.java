package org.codehaus.cake.cache.store;

import java.io.File;
import java.io.IOException;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.store.jdbm.JdbmCacheStore;
import org.codehaus.cake.cache.store.jdbm.JdbmCacheStoreConfiguration;

public class CacheStoreConfiguration<K, V> {

	private Object store;

	/**
	 * Useful for testing.
	 * <p>
	 * Currently this method will create temporary a single file using
	 * {@link File#createTempFile(String, String)} to store elements. However,
	 * the format may change in subsequent releases.
	 * 
	 * @return
	 */
	public CacheStoreConfiguration<K, V> setStoreTemporary() {
		JdbmCacheStoreConfiguration conf = new JdbmCacheStoreConfiguration();
		try {
			conf.setLocation(File.createTempFile(Cache.class.getPackage()
					.getName(), "tmpcache"));
		} catch (IOException e) {
			throw new RuntimeException("could not create temporary file", e);
		}
		store = new JdbmCacheStore(conf);
		return this;
	}

	public CacheStoreConfiguration<K, V> setStore(SimpleCacheStore<K, V> store) {
		this.store = store;
		return this;
	}

	public Object getStore() {
		return store;
	}
}
