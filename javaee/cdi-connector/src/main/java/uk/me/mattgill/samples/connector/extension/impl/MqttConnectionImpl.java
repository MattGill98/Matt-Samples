package uk.me.mattgill.samples.connector.extension.impl;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import uk.me.mattgill.samples.connector.extension.api.MqttConnection;
import uk.me.mattgill.samples.connector.extension.api.QoS;

public class MqttConnectionImpl implements MqttConnection {

	private MqttClient client;

	MqttConnectionImpl(MqttClient client) {
		this.client = client;
	}

	@Override
	public void publish(String topicName, String message, QoS qos) {
		try {
			client.publish(topicName, message.getBytes(), qos.getValue(), false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

}