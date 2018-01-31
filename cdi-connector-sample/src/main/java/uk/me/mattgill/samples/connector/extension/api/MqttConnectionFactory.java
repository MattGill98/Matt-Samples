package uk.me.mattgill.samples.connector.extension.api;

public interface MqttConnectionFactory extends AutoCloseable {

    MqttConnection getConnection();

}