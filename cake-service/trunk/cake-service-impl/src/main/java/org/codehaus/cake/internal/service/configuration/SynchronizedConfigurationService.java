package org.codehaus.cake.internal.service.configuration;

import java.util.Collection;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.WithAttributes;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.service.ContainerConfiguration;

public class SynchronizedConfigurationService implements ConfigurationService {
    private volatile AttributeMap defaults = new DefaultAttributeMap();
    private final Collection<ConfigurableService> services;

    public SynchronizedConfigurationService(ContainerConfiguration<?> configurations, Composer composer) {
        for (Object o : configurations.getConfigurations()) {
            if (o instanceof WithAttributes) {
                WithAttributes c = (WithAttributes) o;
                AttributeMap validated = Attributes.validatedAttributeMap(c.getAttributes());
                defaults.putAll(validated);
            }
        }
        services = composer.getAll(ConfigurableService.class);
        updateAll(defaults);
    }

    /** {@inheritDoc} */
    public synchronized <T> void update(Attribute<T> attribute, T value) {
        updateAll(attribute.singleton(value));
    }

    /** {@inheritDoc} */
    public AttributeMap getAttributes() {
        return defaults;
    }

    public void updateAll(AttributeMap attributes) {
        synchronized (this) {
            update(attributes);
            defaults = new DefaultAttributeMap(defaults);
            for (ConfigurableService cs : services) {
                cs.processUpdate(defaults);
            }
        }
    }

    private void update(AttributeMap attributes) {
        AttributeMap validated = Attributes.validatedAttributeMap(attributes);
        AttributeMap map = new DefaultAttributeMap(defaults);
        map.putAll(validated);
        defaults = map;
    }
}
