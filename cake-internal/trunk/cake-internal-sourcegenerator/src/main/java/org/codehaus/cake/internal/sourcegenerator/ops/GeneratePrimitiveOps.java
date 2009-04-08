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
package org.codehaus.cake.internal.sourcegenerator.ops;

import java.io.Serializable;

import org.codehaus.cake.internal.sourcegenerator.AbstractFileGenerator;
import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.SingleFileGenerator;
import org.junit.Test;

public class GeneratePrimitiveOps{

    @Test
    public void generatePublicMethods() throws Exception {
        SingleFileGenerator g = new SingleFileGenerator(AbstractFileGenerator.FILE_OPS_JAVA, "util.ops.PrimitiveOps");
        g.addImport("static org.codehaus.cake.util.ops.Ops.*");
        g.addImport("static org.codehaus.cake.internal.util.ops.InternalPrimitiveOps.*");
        g.generateWithBody(GenerationType.BIG_4);
    }

    @Test
    public void generateTest() throws Exception {
        for (GenerationType cl : GenerationType.BIG_4) {
            SingleFileGenerator g = new SingleFileGenerator(AbstractFileGenerator.FILE_OPS_TEST,
                    "internal.util.ops.InternalPrimitiveOpsTYPETest", cl);
            g.generate();
        }
    }

    @Test
    public void generateInternal() throws Exception {
        SingleFileGenerator g = new SingleFileGenerator(AbstractFileGenerator.FILE_OPS_JAVA,
                "internal.util.ops.InternalPrimitiveOps");
        g.addImport(Serializable.class);
        g.addImport("static org.codehaus.cake.util.ops.Ops.*");
        g.generateWithBody(GenerationType.BIG_4);
    }
}
