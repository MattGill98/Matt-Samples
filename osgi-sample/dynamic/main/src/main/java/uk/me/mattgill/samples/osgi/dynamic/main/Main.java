package uk.me.mattgill.samples.osgi.dynamic.main;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.me.mattgill.samples.osgi.dynamic.number.api.NumberGenerator;

public class Main implements Runnable, Closeable{

    private Logger logger = Logger.getLogger(Main.class.getName());

    private ScheduledExecutorService executor;
    private NumberGenerator generator;

    public Main(NumberGenerator generator) {
        executor = Executors.newSingleThreadScheduledExecutor();
        this.generator = generator;
    }

    public void setGenerator(NumberGenerator generator) {
        this.generator = generator;
        logger.info("Loaded new generator!");
    }

    @Override
    public void run() {
        executor.scheduleWithFixedDelay(() -> {
            try {
                logger.log(Level.INFO, "Generated number: {0}.", generator.getNumber());
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Error whilst getting service.");
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}