package org.codehaus.cake.internal.sourcegenerator.ops;

import java.io.Serializable;

import org.codehaus.cake.internal.sourcegenerator.AbstractFileGenerator;
import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.SingleFileGenerator;
import org.junit.Test;

public class GeneratePrimitivePredicates {

    @Test
    public void generatePublicMethods() throws Exception {
        SingleFileGenerator g = new SingleFileGenerator(AbstractFileGenerator.FILE_OPS_JAVA, "ops.PrimitivePredicates");
        g.addImport("static org.codehaus.cake.ops.Ops.*");
        g.addImport("static org.codehaus.cake.internal.ops.InternalPrimitivePredicates.*");
        g.generateWithBody(GenerationType.BIG_4);
    }

    @Test
    public void generateTest() throws Exception {
        for (GenerationType cl : GenerationType.BIG_4) {
            SingleFileGenerator g = new SingleFileGenerator(AbstractFileGenerator.FILE_OPS_TEST,
                    "internal.ops.InternalPrimitivePredicatesTYPETest", cl);
            g.generate();
        }
    }

    @Test
    public void generateInternal() throws Exception {
        SingleFileGenerator g = new SingleFileGenerator(AbstractFileGenerator.FILE_OPS_JAVA,
                "internal.ops.InternalPrimitivePredicates");
        g.addImport(Serializable.class);
        g.addImport("static org.codehaus.cake.ops.Ops.*");
        g.generateWithBody(GenerationType.BIG_4);
    }
}
