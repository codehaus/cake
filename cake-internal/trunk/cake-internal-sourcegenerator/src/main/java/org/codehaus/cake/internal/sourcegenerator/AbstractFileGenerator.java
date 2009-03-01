package org.codehaus.cake.internal.sourcegenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class AbstractFileGenerator {
    public static final String FILE_OPS_TEST = "cake-util/cake-util-ops/src/test/java/";
    public static final String FILE_OPS_JAVA = "cake-util/cake-util-ops/src/main/java/";
    static final String HEADER = "common/header.vm";
    final VelocityContext context = new VelocityContext();
    final List<String> imports = new ArrayList<String>();
    final String destination;

    AbstractFileGenerator(String destination) {
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "cake-internal-sourcegenerator/src/resources/vm");
        try {
            Velocity.init(p);
        } catch (Exception e) {
            throw new Error(e);
        }
        this.destination = destination;
    }

    String createFile() {
        String file = destination + "/" + context.get("package").toString().replace('.', '/') + "/";
        file += context.get("this").toString() + ".java";
        File f = new File(file);
        f.getParentFile().mkdirs();
        return file;
    }

    public void addImport(Class<?> clazz) {
        addImport(clazz.getCanonicalName());
    }

    public void addImport(String clazz) {
        if (clazz.contains(".") && !clazz.startsWith(Integer.class.getCanonicalName())) {
            imports.add(clazz); // don't import classes in java lang
        }
    }

    public VelocityContext context() {
        return context;
    }
}
