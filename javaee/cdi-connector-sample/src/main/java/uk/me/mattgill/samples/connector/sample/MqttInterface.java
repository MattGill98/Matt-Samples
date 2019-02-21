package uk.me.mattgill.samples.connector.sample;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import uk.me.mattgill.samples.connector.extension.api.MqttConnection;
import uk.me.mattgill.samples.connector.extension.api.MqttConnectionFactory;
import uk.me.mattgill.samples.connector.extension.api.MqttInbound;
import uk.me.mattgill.samples.connector.extension.api.MqttOutbound;
import uk.me.mattgill.samples.connector.extension.api.QoS;

@ApplicationScoped
public class MqttInterface {

    private Logger logger = Logger.getLogger(MqttInterface.class.getName());

    @Resource
    private ManagedScheduledExecutorService executor;

    @Inject @MqttOutbound
    private MqttConnectionFactory factory;

    public void onMessage(@Observes @MqttInbound(topic = "uk/me/mattgill/test") String message) {
        logger.log(Level.INFO, message);
    }

    public void scheduleSending(@Observes @Initialized(ApplicationScoped.class) Object init) {
        executor.scheduleWithFixedDelay(() -> {
            try {
                MqttConnection conn = factory.getConnection();
                conn.publish("uk/me/mattgill/test", UUID.randomUUID().toString(), QoS.AT_LEAST_ONCE);
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Exception while sending message.", t);
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

}