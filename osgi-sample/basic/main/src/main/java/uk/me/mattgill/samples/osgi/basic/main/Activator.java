package uk.me.mattgill.samples.osgi.basic.main;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import uk.me.mattgill.samples.osgi.basic.number.api.NumberGenerator;

public class Activator implements BundleActivator {

    private Logger logger = Logger.getLogger(Activator.class.getName());

    private Main main;

    @Override
    public void start(BundleContext ctx) throws Exception {
        logger.info("Main bundle started!");

        ServiceReference<?> generatorReference = ctx.getServiceReference(NumberGenerator.class.getName());
        if (generatorReference == null) {
            throw new Exception("There was no number generator registered.");
        }
        NumberGenerator generator = (NumberGenerator) ctx.getService(generatorReference);

        main = new Main(generator);
        main.run();
    }

    @Override
    public void stop(BundleContext ctx) throws Exception {
        main.close();
        logger.info("Main bundle stopped!");
    }

}