package uk.me.mattgill.samples.ejb.remote.sample.ejb;

import java.util.logging.Logger;

import javax.ejb.Stateless;

@Stateless
public class InsecureBean implements Bean {

    private static final Logger LOGGER = Logger.getLogger(InsecureBean.class.getName());

    @Override
    public void doSomething() {
        LOGGER.info("Returning message from insecure bean!");
    }

}