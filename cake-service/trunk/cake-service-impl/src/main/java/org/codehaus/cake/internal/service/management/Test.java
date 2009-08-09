package org.codehaus.cake.internal.service.management;

import org.codehaus.cake.cache.SynchronizedCache;

public class Test {
    public static void main(String[] args) {
        SynchronizedCache sc = new SynchronizedCache();
        System.out.println(sc.getName());
        SynchronizedCache sc2 = new SynchronizedCache();
        System.out.println(sc2.getName());
    }
}
