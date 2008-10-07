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
package org.codehaus.cake.internal.sourcegenerator.ops;

import java.io.Serializable;
import java.util.Arrays;

import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.GeneratorBuilder;

public class GeneratePredicates extends GeneratorBuilder {

    public static void main(String[] args) throws Exception {
        new GeneratePredicates().generate();
    }

    public void generate() throws Exception {
        for (GenerationType cl : Arrays.asList(GenerationType.DOUBLE, GenerationType.FLOAT, GenerationType.INT,
                GenerationType.LONG)) {

            GeneratorBuilder gb = new GeneratorBuilder();
            gb.setPackage("org.codehaus.cake.ops");
            gb.addImport(Serializable.class);
            gb.addImport("static org.codehaus.cake.ops.Ops.*");
            gb.setClassName(cl.getTypeCap() + "Predicates");
            cl.add(gb.context());
            gb.generate("ops/TYPEpredicates.vm", "cake-ops/src/main/java/");

            gb = new GeneratorBuilder();
            gb.setPackage("org.codehaus.cake.ops");
            gb.setClassName(cl.getTypeCap() + "PredicatesTest");
            cl.add(gb.context());
            gb.generate("ops/TYPEpredicatesTest.vm", "cake-ops/src/test/java/");
        }
    }
}
