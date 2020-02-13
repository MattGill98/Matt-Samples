package uk.me.mattgill.samples.ejb.timers;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

@Startup
@Singleton
public class ProgrammaticTimer implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final transient Logger LOGGER = Logger.getLogger(ProgrammaticTimer.class.getName());

    @Resource
    private transient TimerService timerService;

    private transient Timer timer;

    @PostConstruct
    public void init() {
        LOGGER.info("Registering programmatic timer...");
        timer = timerService.createTimer(20000, 20000, "Programmatic timer to run every 20 seconds");
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("Deregistering programmatic timer...");
        timer.cancel();
    }

    @Timeout
    public void sendEvent() {
        LOGGER.info(String.format("Programmatic timer triggered at %s.", new Date()));
    }

}