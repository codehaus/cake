package org.codehaus.cake.service.common.configuration;

import java.util.Set;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.WithAttributes;

public interface RuntimeConfigurationService extends WithAttributes {
    // Attributes: Updateable inherited,
    // Skal vi have træ configuration
    // Hvad med configuration af grupper, f.eks.
    // vi har defineret 
    AttributeMap getInheritedConfiguration();

    AttributeMap getInheritedUpdateableConfiguration();

    /**
     * @return a set of attributes that can be changed at runtime
     */
    Set<Attribute<?>> getUpdateable();

    /**
     * @param attribute
     *            the attribute to update
     * @param value
     *            the new value of the attribute
     * @return this configuration service
     * @throws UnsupportedOperationException
     *             if the specified attribute cannot be updated at runtime
     */
    <T> RuntimeConfigurationService update(Attribute<T> attribute, T value);

    /**
     * Updates
     * <p>
     * A failure encountered while attempting to update any of the attributes in the specified attribute will result in
     * neither of the attributes being changed.
     * <p>
     * The behavior of this operation is undefined if the specified attribute map is modified while the operation is in
     * progress.
     * 
     * @param attributes
     * @return
     * @throws UnsupportedOperationException
     *             if any of the specified attribute cannot be updated at runtime
     */
    RuntimeConfigurationService updateAll(AttributeMap attributes);
}
