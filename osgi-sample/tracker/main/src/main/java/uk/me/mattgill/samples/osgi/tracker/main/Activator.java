package uk.me.mattgill.samples.osgi.tracker.main;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import uk.me.mattgill.samples.osgi.tracker.number.api.NumberGenerator;

public class Activator implements BundleActivator {

    private Logger logger = Logger.getLogger(Activator.class.getName());

    private Main main;

    @Override
    public void start(BundleContext ctx) throws Exception {
        logger.info("Main bundle started!");

        ServiceTracker<NumberGenerator, NumberGenerator> tracker = new ServiceTracker<>(ctx, NumberGenerator.class, null);
        tracker.open();

        main = new Main(tracker);
        main.run();
    }

    @Override
    public void stop(BundleContext ctx) throws Exception {
        main.close();
        logger.info("Main bundle stopped!");
    }

}