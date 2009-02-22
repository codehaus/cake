package org.codehaus.cake.internal.sourcegenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

public class SingleFileGenerator extends AbstractFileGenerator {
    private final String file;
    private final String template;

    public SingleFileGenerator(String destination, String name) {
        super(destination);
        String packageName = "org.codehaus.cake." + name.substring(0, name.lastIndexOf("."));
        context.put("package", packageName);
        context.put("this", name.substring(name.lastIndexOf(".") + 1, name.length()));
        this.file = createFile();
        template = name.replace(".", "/") + ".vm";
    }

    public SingleFileGenerator(String destination, String name, GenerationType type) {
        super(destination);
        String packageName = "org.codehaus.cake." + name.substring(0, name.lastIndexOf("."));
        context.put("package", packageName);
        String className = name.replace("TYPE", type.getTypeCap());
        context.put("this", className.substring(className.lastIndexOf(".") + 1, className.length()));
        type.add(context);
        this.file = createFile();
        template = name.replace(".", "/") + ".vm";
    }

    private String createBody(String template, Iterable<GenerationType> iter) throws Exception {
        StringWriter body = new StringWriter();
        Template t = Velocity.getTemplate(template);
        for (GenerationType gt : iter) {
            Context c = new VelocityContext(context);
            gt.add(c);
            t.merge(c, body);
        }
        return body.getBuffer().toString();
    }

    public void generate() throws Exception {
        Writer writer = new BufferedWriter(new FileWriter(file));
        context.put("Id", "$Id");
        context.put("imports", imports);
        Velocity.getTemplate(HEADER).merge(context, writer);
        Velocity.getTemplate(template).merge(new VelocityContext(context), writer);
        writer.flush();
        writer.close();
    }

    public void generateWithBody(Iterable<GenerationType> iter) throws Exception {
        Writer writer = new BufferedWriter(new FileWriter(file));
        context.put("Id", "$Id");
        context.put("imports", imports);
        Velocity.getTemplate(HEADER).merge(context, writer);
        context.put("body", createBody(template.replace(".vm", "_.vm"), iter));
        Velocity.getTemplate(template).merge(new VelocityContext(context), writer);
        writer.flush();
        writer.close();
    }

}
