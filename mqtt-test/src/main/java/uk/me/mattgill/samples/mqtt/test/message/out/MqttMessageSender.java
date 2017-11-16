package uk.me.mattgill.samples.mqtt.test.message.out;

import fish.payara.cloud.connectors.mqtt.api.MQTTConnection;
import fish.payara.cloud.connectors.mqtt.api.MQTTConnectionFactory;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport;
import org.slf4j.Logger;

@ConnectionFactoryDefinition(name = "java:module/env/MQTTConnectionFactory",
        description = "MQTT Connection Factory",
        interfaceName = "fish.payara.cloud.connectors.mqtt.api.MQTTConnectionFactory",
        resourceAdapter = "mqtt-rar-0.1.0",
        minPoolSize = 2,
        maxPoolSize = 2,
        transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction,
        properties = {
            "serverURIs=tcp://test.mosquitto.org:1883",
            "cleanSession=true"
        })
@Stateless
public class MqttMessageSender {

    @Resource(lookup = "java:module/env/MQTTConnectionFactory")
    private MQTTConnectionFactory factory;

    @Inject
    private Logger logger;

    @Schedule(hour = "*", minute = "*", second = "*/5", persistent = false)
    public void fireEvent() {
        
        String message = UUID.randomUUID().toString();
        
        try (MQTTConnection conn = factory.getConnection()) {
            conn.publish("fish/payara/cloud/test/mosquitto", message.getBytes(), 1, false);
        } catch (Exception ex) {
            logger.error("Error sending message", ex);
        }
    }

}
