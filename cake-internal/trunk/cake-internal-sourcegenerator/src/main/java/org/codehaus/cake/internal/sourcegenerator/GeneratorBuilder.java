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
package org.codehaus.cake.internal.sourcegenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

public class GeneratorBuilder {
    String packageName;
    List<String> imports = new ArrayList<String>();
    // public static final String VM =
    // "cake-internal/cake-internal-sourcegenerator/src/resources/vm/";
    public static final String VM = "";
    public static final String HEADER = "common/header.vm";

    public GeneratorBuilder() {
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", "cake-internal-sourcegenerator/src/resources/vm");
        try {
            Velocity.init(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GeneratorBuilder setPackage(String packageName) {
        context.put("package", packageName);
        return this;
    }

    public GeneratorBuilder setClassName(String name) {
        context.put("this", name);
        return this;
    }

    private final VelocityContext context = new VelocityContext();

    public VelocityContext context() {
        return context;
    }

    public GeneratorBuilder addImport(Class<?> clazz) {
        return addImport(clazz.getCanonicalName());
    }

    public GeneratorBuilder addImport(String clazz) {
        if (clazz.contains(".") && !clazz.startsWith(Integer.class.getCanonicalName())) {
            // don't import classes in java lang
            imports.add(clazz);
        }
        return this;
    }

    public void generate(String templateFile, String target) throws Exception {
        String t = target + "/" + context.get("package").toString().replace('.', '/') + "/";
        t += context.get("this").toString() + ".java";
        File f = new File(t);
        f.getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(t));
        context.put("Id", "$Id");
        context.put("imports", imports);
        Template header = Velocity.getTemplate(HEADER);
        header.merge(context, writer);

        Template template = Velocity.getTemplate(templateFile);
        template.merge(context, writer);

        writer.flush();
        writer.close();
    }

    public void generateMany(String templateFile, String target, Iterable<GenerationType> iter) throws Exception {
        String t = target + "/" + context.get("package").toString().replace('.', '/') + "/";
        t += context.get("this").toString() + ".java";
        File f = new File(t);
        f.getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(t));
        context.put("Id", "$Id");
        context.put("imports", imports);
        Template template = Velocity.getTemplate(templateFile);

        for (GenerationType gt : iter) {
            Context c = new VelocityContext(context);
            gt.add(c);
            template.merge(c, writer);
        }

        writer.flush();
        writer.close();
    }
}
