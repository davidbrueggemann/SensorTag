/**
 * 
 */
package de.gebit.sensortag.util;

import java.util.List;

import tinyb.BluetoothDevice;
import tinyb.BluetoothManager;

/**
 * @author parallels
 *
 */
public class TinybUtil {

	/*
	 * To start looking of the device, we first must initialize the TinyB
	 * library. The way of interacting with the library is through the
	 * BluetoothManager. There can be only one BluetoothManager at one time, and
	 * the reference to it is obtained through the getBluetoothManager method.
	 */
	private static BluetoothManager btManager;

	static BluetoothManager getBluetoothManagerSingleton() {
		if (btManager == null) {
			btManager = BluetoothManager.getBluetoothManager();
		}
		return btManager;
	}

	/*
	 * After discovery is started, new devices will be detected. We can get a
	 * list of all devices through the manager's getDevices method. We can the
	 * look through the list of devices to find the device with the MAC which we
	 * provided as a parameter. We continue looking until we find it, or we try
	 * 15 times (1 minutes).
	 */
	public static BluetoothDevice getDevice(String address) throws InterruptedException {
		BluetoothDevice sensor = null;
		for (int i = 0; (i < 15) /* && running */; ++i) {
			List<BluetoothDevice> list = getBluetoothManagerSingleton().getDevices();
			if (list == null)
				return null;

			for (BluetoothDevice device : list) {
				 printDevice(device);

				/*
				 * Here we check if the address matches.
				 */
				if (device.getAddress().equals(address))
					sensor = device;
			}

			if (sensor != null) {
				return sensor;
			}
//			Thread.sleep(4000);
		}
		return null;
	}

	public static void printDevice(BluetoothDevice device) {
		if (device == null) {
			System.err.println("Given device is null. ");
		} else {
			System.out.print("Address = " + device.getAddress());
			System.out.print(" Name = " + device.getName());
			System.out.print(" Connected = " + device.getConnected());
			System.out.println();
		}
	}
}
