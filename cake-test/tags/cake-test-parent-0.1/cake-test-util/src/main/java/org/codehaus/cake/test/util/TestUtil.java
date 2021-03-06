/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.test.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.jmock.Mockery;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: TestUtil.java 521 2007-12-22 19:15:11Z kasper $
 */
public class TestUtil {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @SuppressWarnings("unchecked")
    public static <V> V dummy(Class<V> arg) {
        return new Mockery().mock(arg);
    }

    public static void assertSingletonSerializable(Object o) {
        assertIsSerializable(o);
        Assert.assertSame(o, serializeAndUnserialize(o));
    }

    public static <T> T serializeAndUnserialize(T o) {
        try {
            return (T) readWrite(o);
        } catch (Exception e) {
            throw new AssertionFailedError(o + " not serialiable");
        }
    }

    public static void assertNotSerializable(Object o) {
        try {
            readWrite(o);
            throw new AssertionFailedError(o + " is serialiable");
        } catch (NotSerializableException nse) {/* ok */
        }
    }

    public static void assertIsSerializable(Object o) {
        // TODO test has serializableID
        try {
            readWrite(o);
        } catch (NotSerializableException e) {
            throw new AssertionFailedError(o + " not serialiable");
        }
    }

    static Object readWrite(Object o) throws NotSerializableException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(20000);
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(bout));
            out.writeObject(o);
            out.close();
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(bin));
            return in.readObject();
        } catch (NotSerializableException nse) {
            throw nse;
        } catch (ClassNotFoundException e) {
            throw new Error(e);// should'nt happen
        } catch (IOException e) {
            throw new Error(e);// should'nt happen
        }
    }
}
