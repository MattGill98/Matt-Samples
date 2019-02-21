package uk.me.mattgill.samples.connector.extension.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.util.AnnotationLiteral;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import uk.me.mattgill.samples.connector.extension.api.MqttInbound;
import uk.me.mattgill.samples.connector.extension.api.QoS;

public class MqttExtension implements Extension {

    /**
     * Stores a map of serverURL -> topic -> QoS.
     */
    private Map<String, Map<String, QoS>> listenerMap = new HashMap<>();

    private List<MqttClient> listeners = new LinkedList<>();

    /**
     * Register all the topics being listened to by observer methods.
     */
    void registerListeners(@Observes ProcessObserverMethod<String, ?> observerMethod) {
        Set<Annotation> qualifiers = observerMethod.getObserverMethod().getObservedQualifiers();
        for (Annotation qualifier : qualifiers) {
            // If the observer is listening for an @MqttInbound String
            if (qualifier instanceof MqttInbound) {
                MqttInbound inbound = (MqttInbound) qualifier;
                Map<String, QoS> topicMap = listenerMap.get(inbound.url());
                if (topicMap == null) {
                    topicMap = new HashMap<>();
                    listenerMap.put(inbound.url(), topicMap);
                }
                topicMap.put(inbound.topic(), inbound.qos());
            }
        }
    }

    // Start the registered listeners
    void afterDeploymentValidation(@Observes AfterDeploymentValidation afterDeploymentValidation,
            BeanManager beanManager) throws MqttException {
        for (String url : listenerMap.keySet()) {
            Map<String, QoS> topicMap = listenerMap.get(url);
            MqttClient client = new MqttClient(url, MqttClient.generateClientId());
            listeners.add(client);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            for (String topic : topicMap.keySet()) {
                QoS qos = topicMap.get(topic);
                client.subscribe(topic, qos.getValue(), new IMqttMessageListener() {

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        beanManager.fireEvent(new String(message.getPayload()), new MqttInboundAnnotationLiteral(url, topic));
                    }
                });
            }
        }
    }

    void beforeShutdown(@Observes BeforeShutdown beforeShutdown) throws MqttException {
        for (MqttClient client : listeners) {
            client.disconnect();
            client.close();
        }
    }

    class MqttInboundAnnotationLiteral extends AnnotationLiteral<MqttInbound> implements MqttInbound {

        private String url;
        private String topic;

        MqttInboundAnnotationLiteral (String url, String topic) {
            this.url = url;
            this.topic = topic;
        }

		@Override
		public String url() {
			return url;
		}

		@Override
		public String topic() {
			return topic;
		}

		@Override
		public QoS qos() {
			return QoS.AT_LEAST_ONCE;
		}
    }

}