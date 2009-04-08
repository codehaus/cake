/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.management.stubs;

import java.io.IOException;

import org.codehaus.cake.management.ManagedAttribute;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class VariousAttributes {
    private String none;

    private boolean readOnly;

    private Integer readWrite;

    private String writeOnly;

    public String getError() {
        throw new LinkageError();
    }

    @ManagedAttribute()
    public String getException2() throws Exception {
        throw new IOException();
    }

    public String getNone() {
        return none;
    }

    public Integer getReadWrite() {
        return readWrite;
    }

    public String getRuntimeException() {
        throw new IllegalMonitorStateException();
    }

    public String getWriteOnly() {
        return writeOnly;
    }

    @ManagedAttribute
    public boolean isReadOnly() {
        return readOnly;
    }

    @ManagedAttribute(defaultValue = "throwError", description = "desc")
    public void setError(String ignore) {
        throw new LinkageError();
    }

    @ManagedAttribute()
    public void setException1(String re) throws Exception {
        throw new IOException();
    }

    public void setNone(String none) {
        this.none = none;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @ManagedAttribute()
    public void setReadWrite(Integer readWrite) {
        this.readWrite = readWrite;
    }

    @ManagedAttribute(defaultValue = "throwRuntimeException", description = "desc")
    public void setRuntimeException(String re) {
        throw new IllegalMonitorStateException();
    }

    @ManagedAttribute(isWriteOnly = true)
    public void setWriteOnly(String writeOnly) {
        this.writeOnly = writeOnly;
    }
}
