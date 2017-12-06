package uk.me.mattgill.samples.log.test.beans.impl;

import javax.enterprise.context.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.mattgill.samples.log.test.beans.PrintingBean;

@Dependent
public class Slf4jBean implements PrintingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jBean.class);

    @Override
    public void printToLog() {
        LOGGER.info("Hello from Slf4j!");
    }

}
