package uk.me.mattgill.samples.jaxrs.cluster.ping.event.entity;

import java.io.Serializable;

/**
 * A wrapper for the {@link TrackerMessage}.
 * It stores the instance that the message is to be sent to.
 */
public class MessageWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

	private TrackerMessage contents;
    private String instanceId;

    public MessageWrapper(TrackerMessage contents, String instanceId) {
        this.contents = contents;
        this.instanceId = instanceId;
    }

    public TrackerMessage getContents() {
        return contents;
    }

    public String getInstanceId() {
        return instanceId;
    }

}