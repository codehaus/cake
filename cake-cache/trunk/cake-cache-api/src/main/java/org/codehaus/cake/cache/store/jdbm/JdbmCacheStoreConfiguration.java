package org.codehaus.cake.cache.store.jdbm;

import java.io.File;

public class JdbmCacheStoreConfiguration {
	private String file;

	private boolean overrideExisting;

	private boolean storeAttributes;

	public String getFile() {
		return file;
	}

	public boolean getStoreAttributes(boolean storeAttributes) {
		return storeAttributes;
	}

	public JdbmCacheStoreConfiguration setLocation(File file) {
		return setLocation(file.getAbsolutePath());
	}

	public JdbmCacheStoreConfiguration setLocation(String file) {
		this.file = file;
		return this;
	}

	public JdbmCacheStoreConfiguration setStoreAttributes(
			boolean storeAttributes) {
		this.storeAttributes = storeAttributes;
		return this;
	}
}
