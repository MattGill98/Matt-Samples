package uk.me.mattgill.samples.osgi.dynamic.main;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import uk.me.mattgill.samples.osgi.dynamic.number.api.NumberGenerator;

public class Activator implements BundleActivator, ServiceListener {

    private Logger logger = Logger.getLogger(Activator.class.getName());

    private Main main;

    private BundleContext ctx;

    @Override
    public void start(BundleContext ctx) throws Exception {
        this.ctx = ctx;
        logger.info("Main bundle started!");

        ServiceReference<?> generatorReference = ctx.getServiceReference(NumberGenerator.class.getName());
        if (generatorReference == null) {
            throw new Exception("There was no number generator registered.");
        }
        NumberGenerator generator = (NumberGenerator) ctx.getService(generatorReference);

        // Register ths bundle as a service listener
        ctx.addServiceListener(this);

        main = new Main(generator);
        main.run();
    }

    @Override
    public void stop(BundleContext ctx) throws Exception {
        main.close();
        logger.info("Main bundle stopped!");
    }

	@Override
	public void serviceChanged(ServiceEvent event) {
		if (event.getType() == ServiceEvent.REGISTERED) {
            logger.info("Service registered.");

            // Get service and load it to the main thread
            ServiceReference<?> ref = event.getServiceReference();
            Object service = ctx.getService(ref);
            if (service instanceof NumberGenerator) {
                NumberGenerator generator = (NumberGenerator) service;
                main.setGenerator(generator);
            }
        }
	}

}