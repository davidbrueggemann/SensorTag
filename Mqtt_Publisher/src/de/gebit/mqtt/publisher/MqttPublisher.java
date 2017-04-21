package de.gebit.mqtt.publisher;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import de.gebit.mqtt.MqttConnector;

public class MqttPublisher {

	// Content that will be delivered over protocol
	// private String content = null;

	// indicates that the message should be delivered once. The message will
	// be persisted to disk, and will be subject to a two-phase
	// acknowledgement across the network.
	private int qos;
	private MqttClient mqttClient;

	private static String clientId = "MqttPublisherSampler";

	private String topic;

	public MqttPublisher(String topic) throws MqttSecurityException, MqttException {
		this.qos = 2;
		this.topic = topic;
		connectToBroker();
	}

	public MqttPublisher(int qualityOfService, String topic) throws MqttSecurityException, MqttException {
		if (qualityOfService > 2 || qualityOfService < 0)
			throw new IllegalArgumentException("Quality of service may not be bigger than 2 or lower than 0!");
		this.qos = qualityOfService;
		this.topic = topic;
		connectToBroker();
	}

	private void connectToBroker() throws MqttSecurityException, MqttException {
		MqttConnector mqttConnector = new MqttConnector(clientId);
		mqttClient = mqttConnector.connectToBroker();
	}

	public void publishMessage(String content) throws MqttPersistenceException, MqttException {
		System.out.println("Publishing message: " + content);

		// Create Message
		MqttMessage message = new MqttMessage(content.getBytes());
		message.setQos(qos);

		// Publish message
		mqttClient.publish(topic, message);
	}

	public void disconnect() throws MqttException{
		mqttClient.disconnect();
	}
}