package uk.me.mattgill.samples.clustered.singleton;

import static java.util.logging.Level.INFO;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class NumberReporter {

    private static final Logger LOGGER = Logger.getLogger(NumberReporter.class.getName());

    public void log(@Observes String message) {
        LOGGER.log(INFO, message);
    }

}