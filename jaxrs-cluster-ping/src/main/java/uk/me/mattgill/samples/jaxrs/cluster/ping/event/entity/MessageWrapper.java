package uk.me.mattgill.samples.jaxrs.cluster.ping.event.entity;

import java.io.Serializable;

public class MessageWrapper implements Serializable {

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