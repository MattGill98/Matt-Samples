package uk.me.mattgill.samples.eventbus.test;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.annotation.FacesConfig;
import javax.faces.context.FacesContext;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@FacesConfig(version = FacesConfig.Version.JSF_2_3)
@Named
@ViewScoped
public class CdiBean implements Serializable {

    private Logger logger = Logger.getLogger(CdiBean.class.getName());

    private List<String> messages;

    @Inject
    private Event<String> event;

    @Inject @Push
    private PushContext push;

    @Inject
    private FacesContext config;

    @PostConstruct
    private void init() {
        messages = new LinkedList<>();
    }

    /**
     * Called when the CDI event is received.
     */
    public void observe(@Observes String message) {
        // Log the message
        logger.info("Message received: \"" + message + "\"");

        // Add the message to the list, and remove the first item if the list is too long
        messages.add(message);
        if (messages.size() > 10) {
            messages.remove(0);
        }

        // Push an update message to the user
        push.send("listMessages");
    }

    /**
     * Call to send a CDI event.
     */
    public void fire() {
        // Default the send message to the current time
        String message = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

        // Attempt to fetch provided message if it's not null
        Object requestMessage = config.getExternalContext().getRequestParameterMap().get("form:message");
        if (requestMessage != null && !requestMessage.equals("")) {
            message = requestMessage.toString();
        }

        logger.info("Message Sending: \"" + message + "\"");
        event.fire(message);
    }

    public List<String> getMessages() {
        return messages;
    }

}