package org.codehaus.cake.internal.sourcegenerator.ops;

import java.io.Serializable;

import org.codehaus.cake.internal.sourcegenerator.AbstractFileGenerator;
import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.SingleFileGenerator;
import org.junit.Ignore;
import org.junit.Test;

public class GeneratePrimitiveComparators {

    @Test
    public void generateTest() throws Exception {
        SingleFileGenerator g = new SingleFileGenerator(AbstractFileGenerator.FILE_OPS_TEST,
                "ops.ComparatorsPrimitiveTest");
        g.addImport(Serializable.class);
        g.addImport("static org.codehaus.cake.ops.Ops.*");
        g.addImport("static org.codehaus.cake.ops.Comparators.*");
        g.generateWithBody(GenerationType.BIG_4);
    }

}
