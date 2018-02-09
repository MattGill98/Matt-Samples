package uk.me.mattgill.samples.osgi.basic.number;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.me.mattgill.samples.osgi.basic.number.api.NumberGenerator;
import uk.me.mattgill.samples.osgi.basic.number.impl.CustomNumberGenerator;

public class Activator implements BundleActivator {

	private Logger logger = Logger.getLogger(Activator.class.getName());

	@Override
	public void start(BundleContext ctx) throws Exception {
		logger.info("Number bundle started!");
		ctx.registerService(NumberGenerator.class.getName(), new CustomNumberGenerator(), null);
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		logger.info("Number bundle stopped!");
	}

}