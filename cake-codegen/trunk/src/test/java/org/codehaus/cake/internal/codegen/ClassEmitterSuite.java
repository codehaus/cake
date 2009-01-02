package org.codehaus.cake.internal.codegen;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { ClassEmitterConstructorTest.class, ClassEmitterFieldsTest.class, ClassEmitterHeaderTest.class,
        ClassEmitterStaticInitializerTest.class })
public class ClassEmitterSuite {

}
