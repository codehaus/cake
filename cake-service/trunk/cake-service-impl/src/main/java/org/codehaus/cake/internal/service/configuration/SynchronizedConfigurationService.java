package org.codehaus.cake.internal.service.configuration;

import java.util.Collection;

import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;
import org.codehaus.cake.util.attribute.ObjectAttribute;
import org.codehaus.cake.util.attribute.WithAttributes;

public class SynchronizedConfigurationService implements ConfigurationService {
    private volatile MutableAttributeMap defaults = new DefaultAttributeMap();
    private final Collection<RuntimeConfigurableService> services;

    static final ObjectAttribute<String> nyAttribute = new ObjectAttribute<String>(String.class) {
        private static final long serialVersionUID = 1L;

        /** @return Preserves singleton property */
        private Object readResolve() {
            return nyAttribute;
        }
    };

    public SynchronizedConfigurationService(ContainerConfiguration configurations, Composer composer) {
        for (Object o : configurations.getConfigurations()) {
            if (o instanceof WithAttributes) {
                WithAttributes c = (WithAttributes) o;
                AttributeMap validated = Attributes.validatedAttributeMap(c.getAttributes());
                defaults.putAll(validated);
            }
        }
        services = composer.getAll(RuntimeConfigurableService.class);
        updateAll(defaults);
    }

    /** {@inheritDoc} */
    public synchronized <T> void update(Attribute<T> attribute, T value) {
        updateAll(attribute.singleton(value));
    }

    /** {@inheritDoc} */
    public MutableAttributeMap getAttributes() {
        return defaults;
    }

    public void updateAll(AttributeMap attributes) {
        synchronized (this) {
            update(attributes);
            defaults = new DefaultAttributeMap(defaults);
            for (RuntimeConfigurableService cs : services) {
                cs.updateConfiguration(defaults);
            }
        }
    }

    private void update(AttributeMap attributes) {
        AttributeMap validated = Attributes.validatedAttributeMap(attributes);
        MutableAttributeMap map = new DefaultAttributeMap(defaults);
        map.putAll(validated);
        defaults = map;
    }
}
