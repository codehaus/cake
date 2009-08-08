package org.codehaus.cake.internal.util;

import java.util.List;

import org.codehaus.cake.test.util.classes.PackageProtectedConstructor;
import org.codehaus.cake.test.util.classes.PublicAbstractClass;
import org.junit.Test;

public class ClassUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void checkInstantiablePrimitiveInt() {
        ClassUtils.checkInstantiable(Integer.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInstantiablePrimitiveInterface() {
        ClassUtils.checkInstantiable(List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInstantiablePrimitiveVoid() {
        ClassUtils.checkInstantiable(Void.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInstantiablePackageProtected() {
        ClassUtils.checkInstantiable(PackageProtectedClass.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInstantiableAbstractClass() {
        ClassUtils.checkInstantiable(PublicAbstractClass.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInstantiableArray() {
        ClassUtils.checkInstantiable(Integer[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInstantiablePackageProtectedConstructor() {
        ClassUtils.checkInstantiable(PackageProtectedConstructor.class);
    }

    static final class PackageProtectedClass {

    }

}
