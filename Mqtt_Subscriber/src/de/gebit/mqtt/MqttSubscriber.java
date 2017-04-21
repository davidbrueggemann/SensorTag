/**
 * 
 */
package de.gebit.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 
 * @author David
 *
 */
public class MqttSubscriber {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String topic = "GEBIT/sensortag/+";
		int qos = 2;
		String broker = "tcp://test.mosquitto.org:1883";
//		String broker = "tcp://iot.eclipse.org:1883";  	// Alternativ broker
		String clientId = "MqttSubscriberSample2";

		MemoryPersistence persistence = new MemoryPersistence();

		try {
			System.out.println("=========================");
			System.out.println("  Start MQTT Subscriber");
			System.out.println("=========================");

			// Client definition as subscriber
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			// Enable async notification
			sampleClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable throwable) {
				}

				// if a message arrived to client it should be printed to
				// console
				public void messageArrived(String t, MqttMessage mqttMessage) throws Exception {
					System.out.println();
					System.out.println("**** NEW MESSAGE *************************");
					System.out.println();
					System.out.println("Topic: " + t);
					System.out.println();
					System.out.println(new String(mqttMessage.getPayload()));
					System.out.println();
					System.out.println("***** EOM ********************************");
				}

				public void deliveryComplete(IMqttDeliveryToken t) {
				}
			});

			// Connect to MQTT-broker
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			System.out.println("Subscribing topic: " + topic);
			sampleClient.subscribe(topic, qos);

			// Teardown
			Thread.sleep(360000); // wait 360 sec for messages
			sampleClient.disconnect();
			System.out.println("Disconnected");
			System.exit(0);
		} catch (MqttException me) {
			System.err.println("reason " + me.getReasonCode());
			System.err.println("msg " + me.getMessage());
			System.err.println("loc " + me.getLocalizedMessage());
			System.err.println("cause " + me.getCause());
			System.err.println("excep " + me);
			me.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println("GEFAHR! GEFAHR!");
			e.printStackTrace();
		}

	}

}
