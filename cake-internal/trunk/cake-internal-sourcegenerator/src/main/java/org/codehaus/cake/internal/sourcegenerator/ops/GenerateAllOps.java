package org.codehaus.cake.internal.sourcegenerator.ops;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { GeneratePrimitiveComparators.class, GeneratePrimitiveOps.class, GeneratePrimitivePredicates.class })
public class GenerateAllOps {

}
