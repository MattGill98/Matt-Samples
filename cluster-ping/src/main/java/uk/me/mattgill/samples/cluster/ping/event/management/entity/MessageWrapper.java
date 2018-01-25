package uk.me.mattgill.samples.cluster.ping.event.management.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import uk.me.mattgill.samples.cluster.ping.event.entity.TrackerMessage;

/**
 * A wrapper for the {@link TrackerMessage}.
 * It stores the instance that the message is to be sent to,
 * as well as visited instances.
 */
public class MessageWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

	private TrackerMessage contents;
    private String targetInstanceId;
    private List<String> visitedInstances;

    public MessageWrapper(TrackerMessage contents, String targetInstanceId) {
        this.contents = contents;
        this.visitedInstances = new LinkedList<>();
        this.targetInstanceId = targetInstanceId;
    }

    public MessageWrapper(MessageWrapper wrapper, String targetInstanceId) {
        this.contents = wrapper.contents;
        this.visitedInstances = wrapper.visitedInstances;
        this.visitedInstances.add(wrapper.getTargetInstanceId());
        this.targetInstanceId = targetInstanceId;
    }

    public TrackerMessage getContents() {
        return contents;
    }

    public List<String> getVisitedInstances() {
        return visitedInstances;
    }

    public String getTargetInstanceId() {
        return targetInstanceId;
    }

}