package uk.me.mattgill.samples.connector.extension.impl;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import uk.me.mattgill.samples.connector.extension.api.MqttConnection;
import uk.me.mattgill.samples.connector.extension.api.MqttConnectionFactory;
import uk.me.mattgill.samples.connector.extension.api.MqttOutbound;

@Dependent
public class MqttConnectionFactoryImpl implements MqttConnectionFactory {

    private MqttClient client;

    @Inject
    MqttConnectionFactoryImpl(InjectionPoint injectionPoint) throws MqttException {
        MqttOutbound outbound = injectionPoint.getAnnotated().getAnnotation(MqttOutbound.class);
        client = new MqttClient(outbound.url(), MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
    }

    @Override
    public MqttConnection getConnection() {
        return new MqttConnectionImpl(client);
    }

	@Override
	public void close() throws Exception {
        client.disconnect();
        client.close();
	}

}