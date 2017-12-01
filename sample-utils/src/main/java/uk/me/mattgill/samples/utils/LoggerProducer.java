package uk.me.mattgill.samples.utils;

import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Produces a {@link java.util.logging.Logger} for injecting.
 */
@Dependent
public class LoggerProducer {

    @Produces
    public Logger producerLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
