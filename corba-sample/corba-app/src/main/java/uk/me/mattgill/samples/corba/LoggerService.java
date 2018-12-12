package uk.me.mattgill.samples.corba;

import java.io.Serializable;

import javax.ejb.Stateless;

import uk.me.mattgill.samples.corba.common.LogResult;
import uk.me.mattgill.samples.corba.common.Logger;

@Stateless
public class LoggerService implements Logger {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger
            .getLogger(LoggerService.class.getName());

    @Override
    public LogResult log(String message) {
        LOGGER.info(message);
        return new LogResult(true, message);
    }

}