package uk.me.mattgill.samples.cluster.ping.event.management;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import uk.me.mattgill.samples.cluster.ping.event.entity.TrackerMessage;
import uk.me.mattgill.samples.cluster.ping.event.hazelcast.HazelcastResources;
import uk.me.mattgill.samples.cluster.ping.event.management.entity.MessageWrapper;

@ApplicationScoped
public class MessageReceiver {

    private final Logger logger = Logger.getLogger(MessageReceiver.class.getName());

    @Inject
    private HazelcastResources resources;

    @Inject
    private MessageSender sender;

    private Map<String, CompletableFuture<TrackerMessage>> waitList;

    @PostConstruct
    public void init() {
        waitList = new HashMap<>();
    }

    /**
     * Receives message events. If the message is for this instance,
     * it checks to see if any request is currently waiting for the message.
     * If it is, it is returned. Otherwise, it is resent to another random instance.
     * 
     * @param messageWrapper the received message.
     */
    private void receiveMessage(@Observes MessageWrapper messageWrapper) {
        // If the message is for the current instance
        if (messageWrapper.getTargetInstanceId().equals(resources.getLocalInstanceId())) {

            // Get the message contents
            TrackerMessage message = messageWrapper.getContents();

            // If there is a thread waiting for this message
            if (waitList.containsKey(message.getId())) {
                logger.log(Level.INFO, "Found in waitlist, completing message {0}.\n", message.toString());
                waitList.get(message.getId()).complete(message);
            } else {
                // Otherwise resend the message to a random instance
                logger.log(Level.INFO, "Not found in waitlist. Resending message {0}.\n", message.toString());
                sender.send(messageWrapper);
            }
        }
    }

    /**
     * Wait to receive a message from the CDI event bus. After the timeout expires, a TimeoutException is thrown.
     * 
     * @param messageId the ID of the message to wait for.
     * @param timeout the amount of time to wait for the message.
     * @param timeout the amount of time to wait for the message.
     * @param unit the timeout unit.
     * 
     * @throws CancellationException if the wait was cancelled.
     * @throws ExecutionException if the wait completed exceptionally.
     * @throws InterruptedException if the current thread was interrupted while waiting.
     * @throws TimeoutException if the wait timed out.
     */
    public TrackerMessage get(String messageId, long timeout, TimeUnit unit)
            throws InterruptedException, TimeoutException, ExecutionException {

        // Registers the message ID as being waited for.
        CompletableFuture<TrackerMessage> future = new CompletableFuture<>();
        waitList.put(messageId, future);

        try {
            return future.get(timeout, unit);
        } finally {
            waitList.remove(messageId);
        }
    }

}