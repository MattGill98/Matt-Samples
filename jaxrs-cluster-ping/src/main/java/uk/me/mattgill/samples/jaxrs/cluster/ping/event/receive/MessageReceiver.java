package uk.me.mattgill.samples.jaxrs.cluster.ping.event.receive;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import uk.me.mattgill.samples.jaxrs.cluster.ping.event.entity.HazelcastResources;
import uk.me.mattgill.samples.jaxrs.cluster.ping.event.entity.MessageWrapper;
import uk.me.mattgill.samples.jaxrs.cluster.ping.event.entity.TrackerMessage;
import uk.me.mattgill.samples.jaxrs.cluster.ping.event.send.MessageSender;

@ApplicationScoped
public class MessageReceiver {

    @Inject
    private HazelcastResources resources;

    @Inject
    private MessageSender sender;

    private Map<UUID, CompletableFuture<TrackerMessage>> waitList;

    @PostConstruct
    public void init() {
        waitList = new HashMap<>();
    }

    public void receiveMessage(@Observes MessageWrapper messageWrapper) {
        // If the message is for the current instance
        if (messageWrapper.getInstanceId().equals(resources.getLocalInstanceId())) {

            // Get the message contents
            TrackerMessage message = messageWrapper.getContents();
            System.out.printf("Message %s is for this instance.\n", message.toString());

            // If there is a thread waiting for this message
            if (waitList.containsKey(message.getId())) {
                System.out.printf("Found in waitlist, completing message %s.\n", message.toString());
                waitList.get(message.getId()).complete(message);
            } else {
                // Otherwise resend the message to a random instance
                System.out.printf("Not found in waitlist. Resending message %s.\n", message.toString());
                sender.send(message);
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
    public TrackerMessage get(UUID messageId, long timeout, TimeUnit unit)
            throws InterruptedException, TimeoutException, ExecutionException {

        CompletableFuture<TrackerMessage> future = new CompletableFuture<>();
        System.out.printf("Adding message %s to waitlist.\n", messageId);
        waitList.put(messageId, future);

        try {
            return future.get(timeout, unit);
        } finally {
            waitList.remove(messageId);
        }
    }

}