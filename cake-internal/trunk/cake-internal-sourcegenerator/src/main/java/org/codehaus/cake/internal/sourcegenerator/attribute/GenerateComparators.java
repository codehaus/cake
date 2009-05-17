package org.codehaus.cake.internal.sourcegenerator.attribute;

import java.util.Comparator;

import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.GeneratorBuilder;

@SuppressWarnings("deprecation")
public class GenerateComparators {
    public static void main(String[] args) throws Exception {
        new GenerateComparators().generate();
        System.out.println("done");
    }

    public void generate() throws Exception {
        GeneratorBuilder gb = new GeneratorBuilder();
        gb.setPackage("org.codehaus.cake.attribute");
        gb.addImport(Comparator.class);
        gb.setClassName("Attributes.tmp");
        // gb.generateMany("attribute/attributemap.vm", "cake-attributes/trunk/cake-attributes/src/main/java/",
        // GenerationType.ALL);
        gb.generateMany("attribute/comparators.vm", "cake-attributes/cake-attributes/src/main/java/",
                GenerationType.ALL);
    }
}
