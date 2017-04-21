package de.gebit.sensortag.util;

import java.util.List;

import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;

public class SensortagServiceUtil {

	/*
	 * Our device should expose a temperature service, which has a UUID we can
	 * find out from the data sheet. The service description of the SensorTag
	 * can be found here:
	 * http://processors.wiki.ti.com/images/a/a8/BLE_SensorTag_GATT_Server.pdf.
	 * The service we are looking for has the short UUID AA00 which we insert
	 * into the TI Base UUID: f000XXXX-0451-4000-b000-000000000000
	 */
	public static BluetoothGattService getService(BluetoothDevice device, String UUID) throws InterruptedException {
		BluetoothGattService tempService = null;
		List<BluetoothGattService> bluetoothServices = null;
		do {
			if(device==null)
				return null;
			
			bluetoothServices = device.getServices();
			if (bluetoothServices == null)
				return null;

			for (BluetoothGattService service : bluetoothServices) {
				if (service.getUUID().equals(UUID))
					tempService = service;
			}
			Thread.sleep(4000);
		} while (bluetoothServices.isEmpty() /* && running */);
		return tempService;
	}

	public static void printDeviceServices(BluetoothDevice device) throws InterruptedException {
		System.out.println("Services exposed by device:");
		List<BluetoothGattService> bluetoothServices = null;
		do {
			bluetoothServices = device.getServices();
			if (bluetoothServices == null)
				System.err.println("No services found for given device!");

			for (BluetoothGattService service : bluetoothServices) {
				System.out.println("UUID: " + service.getUUID());
			}
			Thread.sleep(500);
		} while (bluetoothServices.isEmpty() /* && running */);
	}
	
	public static BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String UUID) {
		List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
		if (characteristics == null)
			return null;

		for (BluetoothGattCharacteristic characteristic : characteristics) {
			if (characteristic.getUUID().equals(UUID))
				return characteristic;
		}
		return null;
	}
	
	
}
