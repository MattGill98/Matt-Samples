package uk.me.mattgill.samples.osgi.web.main;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private Logger logger = Logger.getLogger(Activator.class.getName());

    @Override
    public void start(BundleContext ctx) throws Exception {
        logger.info("Main bundle started!");
    }

    @Override
    public void stop(BundleContext ctx) throws Exception {
        logger.info("Main bundle stopped!");
    }

}