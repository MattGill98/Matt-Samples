package uk.me.mattgill.samples.connector.extension.api;

public interface MqttConnection {

    void publish(String topicName, String message, QoS qos);

}