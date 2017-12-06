package uk.me.mattgill.samples.log.test.beans.impl;

import javax.enterprise.context.Dependent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.me.mattgill.samples.log.test.beans.PrintingBean;

@Dependent
public class Log4jBean implements PrintingBean {

    private final static Logger LOGGER = LogManager.getLogger(Log4jBean.class);

    @Override
    public void printToLog() {
        LOGGER.info("Hello from Log4j!");
    }

}
