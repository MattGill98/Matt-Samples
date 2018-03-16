package uk.me.mattgill.samples.cluster.ping.event.entity;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;
import java.util.UUID;

public class TrackerMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String message;
    private final Queue<String> route;
    private final String routeString;

    public TrackerMessage(String message, Collection<String> route) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.route = new ArrayDeque<>(route);
        this.routeString = Arrays.toString(route.toArray());
    }

    public String getDestination() {
        return route.poll();
    }

    /**
     * @return a String representation of the message.
     */
    @Override
    public String toString() {
        return String.format("{id = '%s', message = '%s', route = '%s'}", id, message, routeString);
    }

    @Override
    public boolean equals(Object compare) {
        if (compare instanceof TrackerMessage) {
            TrackerMessage message = (TrackerMessage) compare;
            if (id.equals(message.id)) {
                return true;
            }
        }
        return false;
    }

}