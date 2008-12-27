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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.GeneratorBuilder;

public class GeneratePrimitiveOps extends GeneratorBuilder {

    public static void main(String[] args) throws Exception {
        new GeneratePrimitiveOps().generate();
        System.out.println("done");
    }

    public void generate() throws Exception {
        GeneratorBuilder gb = new GeneratorBuilder();
        gb.setPackage("org.codehaus.cake.ops");
        gb.addImport(Comparator.class);
        gb.setClassName("PrimitiveOps.tmp");
        // gb.generateMany("attribute/attributemap.vm", "cake-attributes/trunk/cake-attributes/src/main/java/",
        // GenerationType.ALL);
        List l = Arrays.asList(GenerationType.DOUBLE, GenerationType.FLOAT, GenerationType.INT, GenerationType.LONG);
        gb.generateMany("ops/TYPEprimitiveOps.vm", "cake-ops/src/main/java/", l);
    }
}
