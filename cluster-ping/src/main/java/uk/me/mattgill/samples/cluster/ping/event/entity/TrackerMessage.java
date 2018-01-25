package uk.me.mattgill.samples.cluster.ping.event.entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.UUID;

/**
 * A message to be propogated across the cluster.
 * Every time the message is deserialised, it increases the ping count
 * as it has been received as a message.
 */
public class TrackerMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String message;
    private int pingCount;

    public TrackerMessage(String message) {
        this.message = message;
        this.pingCount = 0;
        this.id = UUID.randomUUID().toString();
    }

    /**
     * @return the ID of the message.
     */
    public String getId() {
        return id;
    }

    /**
     * @return a String representation of the message.
     */
    @Override
    public String toString() {
        return String.format("{id = '%s', message = '%s', pingCount = '%d'}", id, message, pingCount);
    }

    /**
     * Whenever this object is received, it has been 'pinged', so the pingCount will increase.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        pingCount++;
    }

}