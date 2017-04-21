/**
 * 
 */
package de.gebit.sensortag.services.temperature;

import de.gebit.sensortag.util.SensortagServiceUtil;
import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;

/**
 * @author parallels
 *
 */
public class SensortagTemperaturService {

	private BluetoothGattCharacteristic temperatureValue;
	private BluetoothGattCharacteristic temperatureConfig;
	private BluetoothGattCharacteristic temperaturePeriod;

	public SensortagTemperaturService(BluetoothDevice sensortagDevice) throws InterruptedException {
		BluetoothGattService tempService = SensortagServiceUtil.getService(sensortagDevice,
				"f000aa00-0451-4000-b000-000000000000");

		if (tempService == null) {
			System.err.println("Device does not provide a temperature service.");
			throw new IllegalStateException("Device does not provide a temperature service.");
		}

		temperatureValue = SensortagServiceUtil.getCharacteristic(tempService, "f000aa01-0451-4000-b000-000000000000");
		temperatureConfig = SensortagServiceUtil.getCharacteristic(tempService, "f000aa02-0451-4000-b000-000000000000");
		temperaturePeriod = SensortagServiceUtil.getCharacteristic(tempService, "f000aa03-0451-4000-b000-000000000000");

		if (temperatureValue == null || temperatureConfig == null || temperaturePeriod == null) {
			System.err.println("Could not find the correct characteristics.");
			throw new IllegalStateException("Device does not provide a temperature service.");
		}

		/*
		 * Turn on the Temperature Service by writing 1 in the configuration
		 * characteristic, as mentioned in the PDF mentioned above. We could
		 * also modify the update interval, by writing in the period
		 * characteristic, but the default 1s is good enough for our purposes.
		 */
		byte[] config = { 0x01 };
		temperatureConfig.writeValue(config);
	}

	public float getObjectTemperature() {
		if (temperatureValue == null || temperatureConfig == null || temperaturePeriod == null) {
			System.err.println("Could not find the correct characteristics.");
			throw new IllegalStateException("Device does not provide a temperature service.");
		}
		byte[] tempRaw = temperatureValue.readValue();
		int objectTempRaw = (tempRaw[0] & 0xff) | (tempRaw[1] << 8);
		return convertCelsius(objectTempRaw);
	}

	public float getAmbientTemperature() {
		if (temperatureValue == null || temperatureConfig == null || temperaturePeriod == null) {
			System.err.println("Could not find the correct characteristics.");
			throw new IllegalStateException("Device does not provide a temperature service.");
		}
		byte[] tempRaw = temperatureValue.readValue();
		int ambientTempRaw = (tempRaw[2] & 0xff) | (tempRaw[3] << 8);
		return convertCelsius(ambientTempRaw);
	}

	private float convertCelsius(int raw) {
		return raw / 128f;
	}
}
