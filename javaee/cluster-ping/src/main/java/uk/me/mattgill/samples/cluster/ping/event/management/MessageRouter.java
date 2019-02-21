package uk.me.mattgill.samples.cluster.ping.event.management;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import fish.payara.micro.cdi.Outbound;
import uk.me.mattgill.samples.cluster.ping.event.entity.TrackerMessage;

@ApplicationScoped
public class MessageRouter {

    @Inject
    private Event<TrackerMessage> event;

    private Map<TrackerMessage, CompletableFuture<TrackerMessage>> waitList = new ConcurrentHashMap<>();

    public TrackerMessage route(TrackerMessage message, int timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<TrackerMessage> future = new CompletableFuture<>();
        waitList.put(message, future);

        send(message, message.getDestination());

        return future.get(timeout, unit);
    }

    private void send(TrackerMessage message, String instance) {
        event.select(new OutboundLiteral(instance)).fire(message);
    }

    @SuppressWarnings("unused")
    private void listen(@Observes TrackerMessage message) {
        String destination = message.getDestination();

        if (destination == null) {
            waitList.get(message).complete(message);
            return;
        }
        send(message, destination);
    }

    private class OutboundLiteral extends AnnotationLiteral<Outbound> implements Outbound {

        private static final long serialVersionUID = 1L;

        private String[] instanceName;

        public OutboundLiteral(String instanceName) {
            this.instanceName = new String[] { instanceName };
        }

        @Override
        public String eventName() {
            return "null";
        }

        @Override
        public boolean loopBack() {
            return true;
        }

        @Override
        public String[] instanceName() {
            return instanceName;
        }

    }

}