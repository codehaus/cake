package org.codehaus.cake.stubber;

import org.codehaus.cake.internal.service.AbstractContainer;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.internal.stubber.bubber.DefaultBubberService;
import org.codehaus.cake.internal.stubber.exceptionhandling.DefaultStubberExceptionService;
import org.codehaus.cake.internal.stubber.tubber.DefaultTubber1Service;
import org.codehaus.cake.internal.stubber.tubber.DefaultTubberService;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.util.Logger;

public abstract class AbstractStubber<T> extends AbstractContainer implements Stubber<T> {
    final InternalExceptionService exceptionService;


    public AbstractStubber(Composer composer) {
        super(composer);
        exceptionService = composer.get(InternalExceptionService.class);

    }

    static Composer newComposer(ContainerConfiguration configuration) {
        Composer composer = new Composer(Stubber.class, configuration);
        composer.registerImplementation(DefaultStubberExceptionService.class);
        composer.registerImplementation(DefaultBubberService.class);
        composer.registerImplementation(DefaultTubberService.class);
        composer.registerImplementation(DefaultTubber1Service.class);
        return composer;
    }

    void fail(InternalExceptionService ies, Logger.Level level, String message, Throwable cause) {
        if (level == Logger.Level.Fatal) {
            if (cause == null) {
                ies.fatal(message);
            } else {
                ies.fatal(message, cause);
            }
        } else if (level == Logger.Level.Error) {
            if (cause == null) {
                ies.error(message);
            } else {
                ies.error(message, cause);
            }
        } else if (level == Logger.Level.Warn) {
            ies.warning(message);
        }

    }
}
