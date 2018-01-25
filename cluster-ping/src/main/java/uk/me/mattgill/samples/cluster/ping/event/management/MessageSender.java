package uk.me.mattgill.samples.cluster.ping.event.management;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import fish.payara.micro.cdi.Outbound;
import uk.me.mattgill.samples.cluster.ping.event.entity.TrackerMessage;
import uk.me.mattgill.samples.cluster.ping.event.hazelcast.HazelcastResources;
import uk.me.mattgill.samples.cluster.ping.event.management.entity.MessageWrapper;

@Dependent
public class MessageSender {

    @Inject
    private HazelcastResources resources;

    @Inject
    @Outbound
    private Event<MessageWrapper> event;

    @Resource
    private ManagedExecutorService executor;

    /**
     * Send to a random instance in the cluster.
     * 
     * @param message the message to send.
     */
    public void send(TrackerMessage message) {
        executor.execute(() -> {
            event.fire(new MessageWrapper(message, resources.getRandomInstanceId()));
        });
    }

}