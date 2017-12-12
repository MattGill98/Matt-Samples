package uk.me.mattgill.samples.kafka.test.message.out;

import fish.payara.cloud.connectors.kafka.api.KafkaConnection;
import fish.payara.cloud.connectors.kafka.api.KafkaConnectionFactory;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport;
import org.apache.kafka.clients.producer.ProducerRecord;

@ConnectionFactoryDefinition(name = "java:module/env/KafkaConnectionFactory",
        description = "Kafka Connection Factory",
        interfaceName = "fish.payara.cloud.connectors.kafka.api.KafkaConnectionFactory",
        resourceAdapter = "kafka-rar-0.1.0",
        minPoolSize = 2,
        maxPoolSize = 2,
        transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction,
        properties = {
            "bootstrapServersConfig=localhost:9092",
            "clientId=test-sender"
        })
@Stateless
public class KafkaMessageSender {

    @Resource(lookup = "java:module/env/KafkaConnectionFactory")
    private KafkaConnectionFactory factory;

    @Inject
    private Logger logger;

    @Schedule(hour = "*", minute = "*", second = "*/5", persistent = false)
    public void fireEvent() {

        String message = UUID.randomUUID().toString();

        try (KafkaConnection conn = factory.createConnection()) {
            conn.send(new ProducerRecord("test", message));
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Error sending message: {0}", ex.getMessage());
        }
    }

}
