package uk.me.mattgill.samples.osgi.web.main;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import uk.me.mattgill.samples.osgi.web.number.api.NumberGenerator;

@ApplicationScoped
public class Main {

    private Logger logger = Logger.getLogger(Main.class.getName());

    @Resource
    private ManagedScheduledExecutorService executor;

    @Inject
    private NumberGenerator generator;

    public void run(@Observes @Initialized(ApplicationScoped.class) Object init) {
        executor.scheduleWithFixedDelay(() -> {
            try {
                logger.log(Level.INFO, "Generated number: {0}.", generator.getNumber());
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Error whilst getting service.");
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

}