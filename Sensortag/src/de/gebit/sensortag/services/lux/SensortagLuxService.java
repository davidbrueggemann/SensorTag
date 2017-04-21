/**
 * 
 */
package de.gebit.sensortag.services.lux;

import de.gebit.sensortag.util.SensortagServiceUtil;
import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;

/**
 * @author parallels
 *
 */
public class SensortagLuxService {
	private BluetoothGattCharacteristic luxPeriod;
	private BluetoothGattCharacteristic luxConfig;
	private BluetoothGattCharacteristic luxValue;

	public SensortagLuxService(BluetoothDevice sensortagDevice) throws InterruptedException {

		BluetoothGattService tempService = SensortagServiceUtil.getService(sensortagDevice,
				"f000aa70-0451-4000-b000-000000000000");
		if (tempService == null) {
			System.err.println("Device does not provide a lux service.");
			throw new IllegalStateException("Device does not provide a lux service.");
		}

		luxValue = SensortagServiceUtil.getCharacteristic(tempService, "f000aa71-0451-4000-b000-000000000000");
		luxConfig = SensortagServiceUtil.getCharacteristic(tempService, "f000aa72-0451-4000-b000-000000000000");
		luxPeriod = SensortagServiceUtil.getCharacteristic(tempService, "f000aa73-0451-4000-b000-000000000000");

		if (luxValue == null || luxConfig == null || luxPeriod == null) {
			System.err.println("Could not find the correct characteristics.");
			throw new IllegalStateException("Device does not provide a lux service.");
		}

		/*
		 * Turn on the lux Service by writing 1 in the configuration
		 * characteristic, as mentioned in the PDF mentioned above. We could
		 * also modify the update interval, by writing in the period
		 * characteristic, but the default 1s is good enough for our purposes.
		 */
		byte[] config = { 0x01 };
		luxConfig.writeValue(config);

	}

	public float getLux() {
		byte[] luxRawData = luxValue.readValue();

		int mantissa;
		int exponent;

		Integer lowerLuxRawData = (int) luxRawData[0] & 0xFF;
		Integer upperLuxRawData = (int) luxRawData[1] & 0xFF;
		
		Integer cummulatedLuxRawData = (upperLuxRawData << 8) + lowerLuxRawData;

		mantissa = cummulatedLuxRawData & 0x0FFF;
		exponent = (cummulatedLuxRawData & 0xF000) >> 12;

		double magnitude = Math.pow(2.0f, exponent);
		return (float) (mantissa * (magnitude * 0.01));
	}

}
