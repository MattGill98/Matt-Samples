package uk.me.mattgill.samples.eventbus.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.annotation.FacesConfig;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

@FacesConfig(version = FacesConfig.Version.JSF_2_3)
@Named
@ApplicationScoped
public class EventbusListener {

    @Inject
    private Logger logger;

    private List<String> messageTimes;

    @Inject
    private Event<Boolean> event;

    @Inject @Push
    private PushContext push;

    @PostConstruct
    private void init() {
        messageTimes = new LinkedList<>();
    }

    /**
     * Called when the CDI event is received.
     */
    public void observe(@Observes Boolean object) {
        // Create message and log the time.
        String message = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        logger.info("Message received at: \"" + message + "\"");

        // Add the message to the list, and remove the first item if the list is too long
        messageTimes.add(message);
        if (messageTimes.size() > 10) {
            messageTimes.remove(0);
        }

        // Push an update message to the user
        push.send("updateTimes");
    }

    /**
     * Call to send a CDI event.
     */
    public void fire() {
        logger.info("Event firing...");
        event.fire(true);
    }

    public List<String> getMessageTimes() {
        return messageTimes;
    }

}