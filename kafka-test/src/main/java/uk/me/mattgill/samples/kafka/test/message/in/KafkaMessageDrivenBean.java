package uk.me.mattgill.samples.kafka.test.message.in;

import fish.payara.cloud.connectors.kafka.api.OnRecord;
import fish.payara.cloud.connectors.kafka.api.KafkaListener;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "bootstrapServersConfig", propertyValue = "localhost:9092"),
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "test-client"),
    @ActivationConfigProperty(propertyName = "groupIdConfig", propertyValue = "test-consumer-group"),
    @ActivationConfigProperty(propertyName = "topics", propertyValue = "test"),
    @ActivationConfigProperty(propertyName = "pollInterval", propertyValue = "1000"),
    @ActivationConfigProperty(propertyName = "autoCommitInterval", propertyValue = "100"),
    @ActivationConfigProperty(propertyName = "retryBackoff", propertyValue = "1000"),
    @ActivationConfigProperty(propertyName = "keyDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),
    @ActivationConfigProperty(propertyName = "valueDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer")
})
public class KafkaMessageDrivenBean implements KafkaListener {

    @Inject
    private Logger logger;

    @OnRecord(topics = {"test"})
    public void getMessageTest(ConsumerRecord record) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Topic", record.topic());
        builder.add("Message", record.toString());
        logger.info(builder.build().toString());
    }
}
