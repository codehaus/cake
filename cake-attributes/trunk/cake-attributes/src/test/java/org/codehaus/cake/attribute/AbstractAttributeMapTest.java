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
package org.codehaus.cake.attribute;

import static org.junit.Assert.fail;

/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 */
public abstract class AbstractAttributeMapTest extends AtrStubs {

    AttributeMap map;

    public void assertImmutable() {
        noClear();
        noRemove();
        noPut();
    }

    public void noClear() {
        try {
            map.clear();
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
    }

    public void noPut() {
        try {
            map.put(O_1, "123");
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.put(B_TRUE, true);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.put(B_1, (byte) 123);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.put(C_1, 'd');
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.put(D_1, 3.4d);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.put(F_1, 123.3f);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.put(I_1, 123);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.put(L_1, 34l);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.put(S_1, (short) 123);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
    }

    public void noRemove() {
        try {
            map.remove(O_1);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.remove(B_TRUE);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.remove(B_1);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.remove(C_1);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.remove(D_1);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.remove(F_1);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.remove(I_1);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.remove(L_1);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
        try {
            map.remove(S_1);
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */
        }
    }

}
