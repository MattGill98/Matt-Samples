package uk.me.mattgill.samples.jaxrs.cluster.ping.event.entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.UUID;

public class TrackerMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String message;
    private int pingCount;

    public TrackerMessage(String message) {
        this.message = message;
        this.pingCount = 0;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TrackerMessage) {
            TrackerMessage c = (TrackerMessage) o;
            if (id.equals(c.id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the details of the message.
     */
    @Override
    public String toString() {
        return String.format("{id = '%6s', message = '%s', pingCount = '%d'}", id.toString(), message, pingCount);
    }

    /**
     * Whenever this object is received, it has been 'pinged', so the pingCount will increase.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        pingCount++;
    }

}