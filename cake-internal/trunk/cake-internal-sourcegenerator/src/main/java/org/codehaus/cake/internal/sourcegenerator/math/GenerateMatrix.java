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
package org.codehaus.cake.internal.sourcegenerator.math;

import java.util.Arrays;
import java.util.Collection;

import org.codehaus.cake.internal.sourcegenerator.GenerationType;
import org.codehaus.cake.internal.sourcegenerator.GeneratorBuilder;

public class GenerateMatrix extends GeneratorBuilder {

    public static void main(String[] args) throws Exception {
        new GenerateMatrix().generate();
        System.out.println("done");
    }

    public void generate() throws Exception {
        Collection<GenerationType> col = Arrays.asList(GenerationType.DOUBLE, /* GenerationType.LONG, */
        /* GenerationType.BIG_DECIMAL, GenerationType.BIG_INTEGER, */GenerationType.COMPLEX);
        for (GenerationType cl : col) {

            GeneratorBuilder gb = new GeneratorBuilder();
            gb.setPackage(getPackageName(cl));
            gb.addImport(cl.getType());
            gb.setClassName(cl.getTypeCap() + "Matrix");
            cl.add(gb.context());
            gb.generate("matrix/TYPEmatrix.vm", "cake-math/src/main/java/");

            gb = new GeneratorBuilder();
            gb.setPackage(getPackageName(cl));
            gb.addImport(cl.getType());
            gb.setClassName("Abstract" + cl.getTypeCap() + "Matrix");
            cl.add(gb.context());
            gb.generate("matrix/AbstractTYPEmatrix.vm", "cake-math/src/main/java/");

            gb = new GeneratorBuilder();
            gb.setPackage(getPackageName(cl));
            gb.addImport(cl.getType());
            gb.setClassName(cl.getTypeCap() + "Vector");
            cl.add(gb.context());
            gb.generate("matrix/TYPEvector.vm", "cake-math/src/main/java/");

            gb = new GeneratorBuilder();
            gb.setPackage(getPackageName(cl));
            gb.addImport(cl.getType());
            gb.setClassName(cl.getTypeCap() + "DenseMatrix");
            gb.context().put("common", VM + "matrix/commonTYPEmatrix.vm");
            cl.add(gb.context());
            gb.generate("matrix/TYPEDenseMatrix.vm", "cake-math/src/main/java/");

            gb = new GeneratorBuilder();
            gb.setPackage(getPackageName(cl));
            gb.addImport(cl.getType());
            gb.setClassName(cl.getTypeCap() + "TridiagonalMatrix");
            gb.context().put("common", VM + "matrix/commonTYPEmatrix.vm");
            cl.add(gb.context());
            gb.generate("matrix/TYPEtridiagonalmatrix.vm", "cake-math/src/main/java/");

            gb = new GeneratorBuilder();
            gb.setPackage(getPackageName(cl));
            gb.addImport(cl.getType());
            gb.setClassName(cl.getTypeCap() + "BandMatrix");
            gb.context().put("common", VM + "matrix/commonTYPEmatrix.vm");
            cl.add(gb.context());
            gb.generate("matrix/TYPEBandMatrix.vm", "cake-math/src/main/java/");

        }
    }

    private static String getPackageName(GenerationType type) {
        String base = "org.codehaus.cake.math.matrix.";
        if (type == GenerationType.LONG) {
            return base + "integer";
        } else if (type == GenerationType.DOUBLE) {
            return base + "real";
        }
        return base + type.getTypeCap().toLowerCase();
    }
}
