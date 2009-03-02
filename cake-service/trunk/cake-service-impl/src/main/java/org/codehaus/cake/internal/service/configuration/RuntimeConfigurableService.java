package org.codehaus.cake.internal.service.configuration;

import java.util.Set;

import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;

public interface RuntimeConfigurableService {
    Set<Attribute<?>> getRuntimeConfigurableAttributes();

    //When configuration service goes public.
    //and a setting can configure multiple services
    //we some kind og prepare / commit to avoid that some services
    // updates the value and then another throws UOE to the update.
  //   public interface PreparableRuntimeConfigurableService {
  //  void prepareUpdateConfiguration(AttributeMap attributes);
    void updateConfiguration(AttributeMap attributes);
    
}
