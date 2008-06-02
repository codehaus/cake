package org.codehaus.cake.cache.service.loading;

import static org.codehaus.cake.cache.service.loading.CacheLoadingService.IS_FORCED;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.test.util.TestUtil;
import org.junit.Test;

public class CacheLoadingServiceTest {
    /**
     * Tests {@link CacheLoadingService#IS_FORCED.
     */
    @Test
    public void attributeCreationTime() {
        assertTrue(IS_FORCED instanceof BooleanAttribute);
        
        TestUtil.assertSingletonSerializable(IS_FORCED);
    }

}
