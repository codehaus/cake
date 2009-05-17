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
package org.codehaus.cake.internal.sourcegenerator.math;

import java.io.Serializable;
import java.util.Arrays;

import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.GeneratorBuilder;

@SuppressWarnings("deprecation")
public class GenerateOps extends GeneratorBuilder {

    public static void main(String[] args) throws Exception {
        new GenerateOps().generate();
        System.out.println("Ops Generated");
    }

    public void generate() throws Exception {
        for (GenerationType cl : Arrays.asList(GenerationType.BIG_DECIMAL, GenerationType.BIG_INTEGER,
                GenerationType.COMPLEX)) {

            GeneratorBuilder gb = new GeneratorBuilder();
            gb.setPackage("org.codehaus.cake.math");
            gb.addImport(Serializable.class);
            gb.addImport("static org.codehaus.cake.ops.Ops.*");
            gb.setClassName(cl.getTypeCap() + "Ops");
            cl.add(gb.context());
            gb.generate("ops/TYPEops.vm", "cake-math/src/main/java/");

            gb = new GeneratorBuilder();
            gb.setPackage("org.codehaus.cake.math");
            gb.addImport("static org.codehaus.cake.ops.Ops.*");
            gb.setClassName(cl.getTypeCap() + "OpsTest");
            cl.add(gb.context());
            gb.generate("ops/TYPEOpsTest.vm", "cake-math/src/test/java/");
        }
    }
}
