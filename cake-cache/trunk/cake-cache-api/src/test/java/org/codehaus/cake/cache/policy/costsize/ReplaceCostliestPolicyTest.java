package org.codehaus.cake.cache.policy.costsize;

import static org.codehaus.cake.cache.CacheEntry.COST;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.Assert.*;

@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class ReplaceCostliestPolicyTest {

    /** The junit mockery. */
    private final Mockery context = new JUnit4Mockery();

    @Test
    public void register() {
        ReplaceCostliestPolicy rbp = new ReplaceCostliestPolicy();
        final InternalCacheAttributeService s = context.mock(InternalCacheAttributeService.class);
        context.checking(new Expectations() {
            {
                one(s).attachToPolicy(with(any(Attribute.class)));// heap index
                one(s).dependOnHard(COST);
            }
        });
        rbp.registerAttribute(s);
    }

    @Test
    public void compare() {
        ReplaceCostliestPolicy rbp = new ReplaceCostliestPolicy();

        final CacheEntry ce1 = context.mock(CacheEntry.class, "1");
        final AttributeMap am1 = new DefaultAttributeMap();

        final CacheEntry ce2 = context.mock(CacheEntry.class, "2");
        final AttributeMap am2 = new DefaultAttributeMap();

        final CacheEntry ce3 = context.mock(CacheEntry.class, "3");
        final AttributeMap am3 = new DefaultAttributeMap();

        context.checking(new Expectations() {
            {
                allowing(ce1).getAttributes();
                will(returnValue(am1));
                allowing(ce2).getAttributes();
                will(returnValue(am2));
                allowing(ce3).getAttributes();
                will(returnValue(am3));
            }
        });
        COST.set(ce1, 1.5);
        COST.set(ce2, 2.5);
        COST.set(ce3, 3.5);

        rbp.add(ce1);
        rbp.add(ce3);
        rbp.add(ce2);
        assertSame(ce3, rbp.evictNext());
        assertSame(ce2, rbp.evictNext());
        rbp.add(ce3);
        assertSame(ce3, rbp.evictNext());
        assertSame(ce1, rbp.evictNext());
        assertNull(rbp.evictNext());
    }
}
