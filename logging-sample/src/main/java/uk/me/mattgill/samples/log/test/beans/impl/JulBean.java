package uk.me.mattgill.samples.log.test.beans.impl;

import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import uk.me.mattgill.samples.log.test.beans.PrintingBean;

@Dependent
public class JulBean implements PrintingBean {

    private static final Logger LOGGER = Logger.getLogger(JulBean.class.getName());

    @Override
    public void printToLog() {
        LOGGER.info("Hello from JUL!");
    }

}
