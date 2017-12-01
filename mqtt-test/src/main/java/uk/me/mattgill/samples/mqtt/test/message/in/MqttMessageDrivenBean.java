package uk.me.mattgill.samples.mqtt.test.message.in;

import fish.payara.cloud.connectors.mqtt.api.MQTTListener;
import fish.payara.cloud.connectors.mqtt.api.OnMQTTMessage;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "serverURIs", propertyValue = "tcp://test.mosquitto.org:1883"),
    @ActivationConfigProperty(propertyName = "cleanSession", propertyValue = "false"),
    @ActivationConfigProperty(propertyName = "automaticReconnect", propertyValue = "true"),
    @ActivationConfigProperty(propertyName = "filePersistence", propertyValue = "false"),
    @ActivationConfigProperty(propertyName = "connectionTimeout", propertyValue = "30"),
    @ActivationConfigProperty(propertyName = "maxInflight", propertyValue = "3"),
    @ActivationConfigProperty(propertyName = "keepAliveInterval", propertyValue = "5"),
    @ActivationConfigProperty(propertyName = "topicFilter", propertyValue = "fish/payara/cloud/test/mosquitto"),
    @ActivationConfigProperty(propertyName = "qos", propertyValue = "1")
})
public class MqttMessageDrivenBean implements MQTTListener {
     
    @Inject
    private Logger logger;
    
    @OnMQTTMessage
    public void getMessageTest(String topic, MqttMessage message) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Topic", topic);
        builder.add("Message", new String(message.getPayload()));
        logger.info(builder.build().toString());
    }
}
