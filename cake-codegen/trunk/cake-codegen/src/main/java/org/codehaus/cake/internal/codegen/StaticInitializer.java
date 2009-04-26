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
package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;

public class StaticInitializer extends AbstractMethod<StaticInitializer> {

    StaticInitializer(ClassEmitter emitter) {
        super(emitter, 0 + Opcodes.ACC_STATIC, "<clinit>", null, Type.VOID_TYPE, new Type[0]);
    }

    void finish() {
        if (last != Operation.RETURN) {
            adaptor.returnValue();
        }
        super.finish();
    }
}
