package org.codehaus.cake.test.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class TestUtilTest {

    @Test(expected = IllegalArgumentException.class)
    public void paramsIAE() {
        TestUtil.params(0, 1, 2, 4, 5, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void paramsIAE1() {
        TestUtil.params(2, 1, 2, 4);// Split and object[] does not fit
    }

    @Test(expected = IllegalArgumentException.class)
    public void paramsIAE2() {
        TestUtil.params(4, 1, 2);// Split and object[] does not fit
    }

    @Test
    public void params1() {
        List<Object[]> col = TestUtil.params(1, 2, 3, 4, 5);// Split and object[] does not fit
        assertEquals(4, col.size());
        assertArrayEquals(new Object[] { 2 }, col.get(0));
        assertArrayEquals(new Object[] { 3 }, col.get(1));
        assertArrayEquals(new Object[] { 4 }, col.get(2));
        assertArrayEquals(new Object[] { 5 }, col.get(3));
    }

    @Test
    public void params2() {
        List<Object[]> col = TestUtil.params(2, 2, 3, 4, 5, 6, 7);// Split and object[] does not fit
        assertEquals(3, col.size());
        assertArrayEquals(new Object[] { 2, 3 }, col.get(0));
        assertArrayEquals(new Object[] { 4, 5 }, col.get(1));
        assertArrayEquals(new Object[] { 6, 7 }, col.get(2));
    }
}
