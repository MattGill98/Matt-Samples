package uk.me.mattgill.samples.clustered.singleton;

import java.io.Serializable;
import java.util.Random;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import fish.payara.cluster.Clustered;

@Singleton
@Clustered
@Startup
public class NumberGenerator implements Serializable {

    private static final long serialVersionUID = 4928344696337609325L;

    private Random random = new Random();

    @Inject
    private Event<String> event;

    @Schedule(hour = "*", minute = "*", second = "*/5")
    public void produceNumber() {
        String report = String.format("Random number generated: %d.", random.nextInt(100) + 1);
        event.fire(report);
    }

}