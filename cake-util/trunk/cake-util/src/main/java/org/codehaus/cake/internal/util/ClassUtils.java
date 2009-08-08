package org.codehaus.cake.internal.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

    /**
     * Returns the single method
     * 
     * @param clazz
     *            the class to check
     * @param annotation
     *            the annotation to look for
     * @return the single method with specified annotation
     * @throws NullPointerException
     *             if the specified annotation is null
     * 
     */
    public static Method checkExactlyOneMethodWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Method> methods = ClassUtils.getMethodsWithAnnotation(clazz, annotation);
        if (methods.size() == 0) {
            throw new IllegalArgumentException("no public methods annotated with @" + annotation.getSimpleName()
                    + " on the specified object");
        } else if (methods.size() > 1) {
            throw new IllegalArgumentException("More then 1 public methods annotated with @"
                    + annotation.getSimpleName() + " on the specified object " + methods);
        }
        return methods.get(0);
    }

    public static Class<?> getParameterizedType(Class<?> onClass, Class<?> iface) {
        Type[] types = onClass.getGenericInterfaces();
        for (Type t : types) {
            if (t.equals(iface)) {
                if (t instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) t;
                    return (Class<?>) (Class) pt.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

    /**
     * Returns all methods that is annotated with the specified annotation.
     * 
     * @param clazz
     *            the class to check
     * @param annotation
     *            the annotation to look for
     * @return a list of all methods that is annotated with the specified annotation
     * @throws NullPointerException
     *             if the specified annotation is null
     * 
     */
    public static List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (annotation == null) {
            throw new NullPointerException("annotation is null");
        }
        ArrayList<Method> result = new ArrayList<Method>();
        Method[] m = clazz.getMethods();
        for (int i = 0; i < m.length; i++) {
            Annotation a = m[i].getAnnotation(annotation);
            if (a != null) {
                result.add(m[i]);
            }
        }
        return result;
    }

    public static void checkInstantiable(Class<?> clazz) {
        if (clazz.isAnnotation()) {
            throw new IllegalArgumentException("The specified class is an annotation and cannot be instantiated");
        } else if (clazz.isInterface()) {
            throw new IllegalArgumentException("The specified class is an interface and cannot be instantiated");
        } else if (clazz.isArray()) {
            throw new IllegalArgumentException("The specified class is an array and cannot be instantiated");
        } else if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("The specified class is a primitive class and cannot be instantiated");
        }
        int modifier = clazz.getModifiers();
        if (Modifier.isAbstract(modifier)) {
            throw new IllegalArgumentException("The specified class is an abstract class and cannot be instantiated");
        } else if (!Modifier.isPublic(modifier)) {
            throw new IllegalArgumentException("The specified class is not a public class and cannot be instantiated");
        }
        if (clazz.getConstructors().length == 0) {
            throw new IllegalArgumentException(
                    "The specified class does not have any public constructors and cannot be instantiated");
        }
    }
}
