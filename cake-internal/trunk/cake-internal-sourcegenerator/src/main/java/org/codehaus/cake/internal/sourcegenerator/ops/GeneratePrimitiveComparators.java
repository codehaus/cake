package org.codehaus.cake.internal.sourcegenerator.ops;

import java.io.Serializable;

import org.codehaus.cake.internal.sourcegenerator.AbstractFileGenerator;
import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.SingleFileGenerator;
import org.junit.Test;

public class GeneratePrimitiveComparators {

    @Test
    public void generateTest() throws Exception {
        SingleFileGenerator g = new SingleFileGenerator(AbstractFileGenerator.FILE_OPS_TEST,
                "util.ops.ComparatorsPrimitiveTest");
        g.addImport(Serializable.class);
        g.addImport("static org.codehaus.cake.util.ops.Ops.*");
        g.addImport("static org.codehaus.cake.util.ops.Comparators.*");
        g.generateWithBody(GenerationType.BIG_4);
    }

}
