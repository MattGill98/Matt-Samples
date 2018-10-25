package uk.me.mattgill.samples.ejb.remote.sample.ejb;

import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import uk.me.mattgill.samples.ejb.remote.sample.service.JaxrsApplication;

@Stateless
public class SecureBean implements Bean {

    private static final Logger LOGGER = Logger.getLogger(SecureBean.class.getName());

    @Override
    @RolesAllowed(JaxrsApplication.SECURE_ROLE)
    public void doSomething() {
        LOGGER.info("Returning message from secure bean!");
    }

}