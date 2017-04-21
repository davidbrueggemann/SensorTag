/**
 * 
 */
package de.gebit.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author parallels
 *
 */
public class MqttConnector {
	// MQTT-test-broker
	private String broker  ;
	// client identifier that is unique on the server being connected to
	private String clientId  ;
	
	private MemoryPersistence persistence = new MemoryPersistence();

	public MqttConnector(String clientId) {
		
		broker = "tcp://test.mosquitto.org:1883";
//		broker = "tcp://iot.eclipse.org:1883";  	// Alternative broker
		this.clientId = clientId;
	}

	public MqttConnector(String content, int qos, String broker, String clientId) {
		super();
		this.broker = broker;
		this.clientId = clientId;
	}
	
	public MqttClient connectToBroker() throws MqttSecurityException, MqttException
	{
		System.out.println("=========================");
		System.out.println("  Start MQTT Publisher");
		System.out.println("=========================");

		// Client definition as publisher
		MqttClient tempClient = new MqttClient(broker, clientId, persistence);

		// Connect to MQTT-broker
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		System.out.println("Connecting to broker: " + broker);
		tempClient.connect(connOpts);
		System.out.println("MQTT Broker Connected");
		System.out.println();
		return tempClient;
	}
	
	public void disconnectBroker(MqttClient mqttClient)
	{
		// Teardown
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			System.out.println("Disconnected with errors:");
			e.printStackTrace();
		}
		System.out.println("Disconnected");
	}
}
