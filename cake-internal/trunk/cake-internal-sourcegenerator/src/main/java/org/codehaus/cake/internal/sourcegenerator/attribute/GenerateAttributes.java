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
package org.codehaus.cake.internal.sourcegenerator.attribute;

import java.io.Serializable;
import java.util.Comparator;

import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.GeneratorBuilder;

@SuppressWarnings("deprecation")
public class GenerateAttributes extends GeneratorBuilder {

    public static void main(String[] args) throws Exception {
        new GenerateAttributes().generate();
        System.out.println("done");
    }

    public void generate() throws Exception {
        for (GenerationType cl : GenerationType.ALL) {
            GeneratorBuilder gb = new GeneratorBuilder();
            gb.setPackage("org.codehaus.cake.attribute");
            gb.addImport(Comparator.class);
            gb.addImport(Serializable.class);
            gb.setClassName(cl.getTypeCap() + "Attribute");
            cl.add(gb.context());
            gb.generate("attribute/TYPEattribute.vm", "cake-attributes/cake-attributes/src/main/java/");
            if (cl != GenerationType.BOOLEAN) {
                gb = new GeneratorBuilder();
                gb.setPackage("org.codehaus.cake.attribute");
                gb.setClassName(cl.getTypeCap() + "AttributeTest");
                cl.add(gb.context());
                gb.generate("attribute/TYPEattributeTest.vm", "cake-attributes/cake-attributes/src/test/java/");
            }
        }
    }
}
