package uk.me.mattgill.samples.ejb.timers;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class AnnotatedTimer implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final transient Logger LOGGER = Logger.getLogger(AnnotatedTimer.class.getName());

    @PostConstruct
    public void init() {
        LOGGER.info("Registering annotated timer...");
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("Deregistering annotated timer...");
    }

    @Schedule(hour = "*", minute = "*", second = "*/20")
    public void timeout() {
        LOGGER.info("Annotated timer triggered at " + new Date());
    }

}