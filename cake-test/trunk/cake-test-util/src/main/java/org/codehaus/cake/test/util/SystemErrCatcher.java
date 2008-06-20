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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public final class SystemErrCatcher {
    private StringBuffer sb = new StringBuffer();

    private final PrintStream old;

    SystemErrCatcher() {
        old = System.err;
        System.setErr(new PrintStream(new MyOutput()));
    }

    public String toString() {
        return sb.toString();
    }

    public void clear() {
        sb = new StringBuffer();
    }

    public void terminate() {
        System.setErr(old);
    }

    public static SystemErrCatcher get() {
        return new SystemErrCatcher();
    }

    private class MyOutput extends OutputStream {
        public void write(int b) throws IOException {
            sb.append((char) b);
        }
    }

}
