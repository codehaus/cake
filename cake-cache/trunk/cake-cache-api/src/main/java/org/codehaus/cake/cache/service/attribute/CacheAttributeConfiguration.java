package org.codehaus.cake.cache.service.attribute;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;

public class CacheAttributeConfiguration {

	private AttributeMap attributes = new DefaultAttributeMap();

	public AttributeMap getAllAttributes() {
		return new DefaultAttributeMap(attributes);
	}

	public <T> CacheAttributeConfiguration add(Attribute<T> a, T defaultValue) {
		a.checkValid(defaultValue);
		if (attributes.contains(a)) {
			throw new IllegalArgumentException(
					"Attribute has already been added [Attribute =" + a + "");
		}
		attributes.put(a, defaultValue);
		return this;
	}

	public CacheAttributeConfiguration add(Attribute... a) {
		for (Attribute aa : a) {
			if (attributes.contains(aa)) {
				throw new IllegalArgumentException(
						"Attribute has already been added [Attribute =" + aa
								+ "");
			}
		}
		for (Attribute aa : a) {
			attributes.put(aa, aa.getDefault());
		}
		return this;
	}
}
