/**
 * 
 */
package de.gebit.mqtt;

import de.gebit.mqtt.publisher.MqttPublisher;
import de.gebit.sensortag.Sensortag;

/**
 * @author parallels
 *
 */
public class Main {

	private static String sensortagAddress = "A0:E6:F8:B6:4D:86";
	private static String topic = "GEBIT/sensortag/1";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			MqttPublisher mqttPublisher = new MqttPublisher(topic);

			Sensortag tempSensortag = new Sensortag(sensortagAddress);
			// tempSensortag.printDeviceServices();
			while (true) {
				// System.out.println("Objecttemperature: " +
				// tempSensortag.getObjectTemperatureValue());
				// System.out.println("Ambienttemperature: " +
				// tempSensortag.getAmbientTemperatureValue());
				// System.out.println("Luxvalue: " +
				// tempSensortag.getLuxValue());

				mqttPublisher.publishMessage(System.lineSeparator() + "Objecttemperature:  "
						+ tempSensortag.getObjectTemperatureValue() + System.lineSeparator() + "Ambienttemperature: "
						+ tempSensortag.getAmbientTemperatureValue() + System.lineSeparator() + "Luxvalue:           "
						+ tempSensortag.getLuxValue());
				System.out.println();
				Thread.sleep(1000);
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
			System.exit(-1);
		}

	}

}
