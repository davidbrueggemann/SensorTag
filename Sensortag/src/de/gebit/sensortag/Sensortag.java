/**
 * 
 */
package de.gebit.sensortag;

import de.gebit.sensortag.services.lux.SensortagLuxService;
import de.gebit.sensortag.services.temperature.SensortagTemperaturService;
import de.gebit.sensortag.util.SensortagServiceUtil;
import de.gebit.sensortag.util.TinybUtil;
import tinyb.BluetoothDevice;

/**
 * @author parallels
 *
 */
public class Sensortag {
	private final BluetoothDevice sensortagDevice;
	private final SensortagTemperaturService temperatureService;
	private final SensortagLuxService luxService;

	public Sensortag(String deviceAdress) throws InterruptedException {
		System.out.println("Scanning for Sensortag: " + deviceAdress);
		sensortagDevice = TinybUtil.getDevice(deviceAdress);
		connect();
		temperatureService = new SensortagTemperaturService(sensortagDevice);
		luxService = new SensortagLuxService(sensortagDevice);
	}

	private boolean connect() {
		System.out.print("Connecting to device: ");
		TinybUtil.printDevice(sensortagDevice);
		if (sensortagDevice != null) {
			boolean tempConnected = sensortagDevice.connect();
			if (tempConnected) {
				System.out.println("Given device is now connected.");
			} else {
				System.err.println("Device not connected!");
			}
			return tempConnected;
		}
		return false;
	}

	public float getObjectTemperatureValue() throws InterruptedException {
		return temperatureService.getObjectTemperature();
	}

	public float getAmbientTemperatureValue() throws InterruptedException {
		return temperatureService.getAmbientTemperature();
	}

	public void printDeviceServices() throws InterruptedException {
		SensortagServiceUtil.printDeviceServices(sensortagDevice);
	}

	public float getLuxValue() throws InterruptedException {
		return luxService.getLux();
	}

}
