package uk.me.mattgill.samples.mqtt.test.message.out;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport;

import fish.payara.cloud.connectors.mqtt.api.MQTTConnection;
import fish.payara.cloud.connectors.mqtt.api.MQTTConnectionFactory;
import uk.me.mattgill.samples.mqtt.test.message.in.MqttMessageDrivenBean;

@ConnectionFactoryDefinition(name = "java:module/env/MQTTConnectionFactory",
        description = "MQTT Connection Factory",
        interfaceName = "fish.payara.cloud.connectors.mqtt.api.MQTTConnectionFactory",
        resourceAdapter = "mqtt-rar-0.2.0",
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

    private Logger logger = Logger.getLogger(MqttMessageDrivenBean.class.getName());

    @Schedule(hour = "*", minute = "*", second = "*/5", persistent = false)
    public void fireEvent() {
        
        String message = UUID.randomUUID().toString();
        
        try (MQTTConnection conn = factory.getConnection()) {
            conn.publish("fish/payara/cloud/test/mosquitto", message.getBytes(), 1, false);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error sending message: {0}", ex.getMessage());
        }
    }

}
