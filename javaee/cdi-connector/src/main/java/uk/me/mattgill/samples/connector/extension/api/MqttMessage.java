package uk.me.mattgill.samples.connector.extension.api;

public class MqttMessage {

    private String topic;
    private String payload;

    public MqttMessage(String topic, byte[] payload) {
        this.topic = topic;
        this.payload = new String(payload);
    }

    public String getTopic() {
        return topic;
    }

    public String getPayload() {
        return payload;
    }

}