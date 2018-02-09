package uk.me.mattgill.samples.osgi.tracker.main;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.util.tracker.ServiceTracker;

import uk.me.mattgill.samples.osgi.tracker.number.api.NumberGenerator;

public class Main implements Runnable, Closeable{

    private Logger logger = Logger.getLogger(Main.class.getName());

    private ScheduledExecutorService executor;
    private ServiceTracker<NumberGenerator, NumberGenerator> tracker;

    public Main(ServiceTracker<NumberGenerator, NumberGenerator> tracker) {
        executor = Executors.newSingleThreadScheduledExecutor();
        this.tracker = tracker;
    }

    @Override
    public void run() {
        executor.scheduleWithFixedDelay(() -> {
            try {
                logger.log(Level.INFO, "Generated number: {0}.", tracker.getService().getNumber());
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