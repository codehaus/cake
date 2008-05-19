package org.codehaus.cake.service.test.tck;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Container.SupportedServices;
import org.codehaus.cake.service.executor.ExecutorsService;

class TckUtil {

    static Class<? extends Container> containerImplementation;
    static Class<? extends ContainerConfiguration> configuration;

    static {
        InputStream is = TckRunner.class.getClassLoader().getResourceAsStream("defaulttestclass");
        if (is != null) {
            try {
                Properties p = new Properties();
                p.load(is);
                containerImplementation = (Class) Class.forName(p.getProperty("container"));
                configuration = (Class) Class.forName(p.getProperty("configuration"));
            } catch (IOException e) {
                throw new Error(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    static boolean isThreadSafe() {
        Set<Class> supportedServices = new HashSet(Arrays.asList(containerImplementation.getAnnotation(
                SupportedServices.class).value()));
        return supportedServices.contains(ExecutorsService.class);
    }

    public static Container newContainer(ContainerConfiguration configuration) {
        try {
            return (Container) configuration.newInstance(containerImplementation);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static ContainerConfiguration newConfiguration() {
        try {
            return configuration.newInstance();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
