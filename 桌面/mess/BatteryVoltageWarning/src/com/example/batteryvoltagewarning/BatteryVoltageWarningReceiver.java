package com.example.batteryvoltagewarning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;
import android.os.UserHandle;
import android.os.Handler;
import android.content.IntentFilter;

public class BatteryVoltageWarningReceiver extends BroadcastReceiver {
	int status;
	int plugged;
	int voltage;
	private static final String STATUS = "status";
	private static final String PLUGGED = "plugged";
	private static final String VOLTAGE = "voltage";
	private static final String TAG = BatteryVoltageWarningReceiver.class
			.getSimpleName();

	/*
	 * private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
	 * String action; int status; int plugged; int voltage; String acString;
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { action
	 * = intent.getAction();
	 * 
	 * if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
	 * Toast.makeText(context, "run*"+getBatteryVoltage().substring(0, 4),
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * }
	 * 
	 * };
	 */

	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();
		// mContext = context;
		Intent newIntent = new Intent(context, TestService.class);
		if ("android.hardware.usb.action.USB_STATE".equals(intent.getAction())) {
			if (intent.getExtras().getBoolean("connected")) {
				context.startService(newIntent);
				Log.d(TAG, "USB_STATE============Connected");
			} else {
				context.stopService(newIntent);
				Log.d(TAG, "USB_STATE============DisConnected");
			}
		}
		/*
		 * 
		 * if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
		 * 
		 * // IntentFilter filter = new IntentFilter(); //
		 * filter.addAction(Intent.ACTION_BATTERY_CHANGED); //
		 * context.registerReceiver(mBroadcastReceiver, filter);
		 * 
		 * context.startService(newIntent);
		 * 
		 * // isRun = true;
		 * 
		 * // String vol = ""; // vol = getBatteryVoltage();// Overvoltage
		 * Undervoltage Good
		 * 
		 * Log.e("Undervoltage", "BatteryVoltage============" +
		 * getBatteryVoltage().substring(0, 4));
		 * 
		 * // Toast.makeText(context, "ACTION_POWER_CONNECTED=Connect", //
		 * Toast.LENGTH_SHORT).show(); // Toast.makeText(context,
		 * getBatteryVoltage().substring(0, 4), // Toast.LENGTH_SHORT).show();
		 * Log.d(TAG, "ACTION_POWER_CONNECTED============usbPlugged");
		 * 
		 * } if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
		 * 
		 * context.stopService(newIntent); //
		 * myHandler.removeCallbacks(myRunnable); // Toast.makeText(context,
		 * "ACTION_POWER_DISCONNECTED", // Toast.LENGTH_SHORT).show(); //
		 * context.unregisterReceiver(mBroadcastReceiver); Log.d(TAG,
		 * "ACTION_POWER_DISCONNECTED===========usbUnPlugged"); }
		 */

		if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
			context.stopService(newIntent);
			Log.d(TAG, "ACTION_SHUTDOWN============");
		}
		/*
		 * if (action.equals(Intent.ACTION_BATTERY_CHANGED)) { status =
		 * intent.getIntExtra(STATUS, 0); plugged = intent.getIntExtra(PLUGGED,
		 * 0); voltage = intent.getIntExtra(VOLTAGE, 0); //String statusString =
		 * ""; switch (status) { case BatteryManager.BATTERY_STATUS_UNKNOWN:
		 * //statusString = getResources().getString(R.string.charger_unknown);
		 * break;
		 * 
		 * case BatteryManager.BATTERY_STATUS_CHARGING: //statusString =
		 * getResources().getString(R.string.charger_charging); break;
		 * 
		 * case BatteryManager.BATTERY_STATUS_DISCHARGING: //statusString =
		 * getResources().getString(R.string.charger_discharging); break;
		 * 
		 * case BatteryManager.BATTERY_STATUS_NOT_CHARGING: //statusString =
		 * getResources().getString(R.string.charger_not_charging); break;
		 * 
		 * case BatteryManager.BATTERY_STATUS_FULL: //statusString =
		 * getResources().getString(R.string.charger_full); break; default:
		 * break;
		 * 
		 * }
		 * 
		 * //acString = ""; switch (plugged) { case
		 * BatteryManager.BATTERY_PLUGGED_AC: //acString =
		 * getResources().getString(R.string.charger_ac_plugged); break; case
		 * BatteryManager.BATTERY_PLUGGED_USB: //acString =
		 * getResources().getString(R.string.charger_usb_plugged);
		 * Toast.makeText(context, "BATTERY_PLUGGED_USB=Connect",
		 * Toast.LENGTH_SHORT).show(); Log.e("BatteryVoltageWarningReceiver",
		 * "==============BATTERY_PLUGGED_USB============"); break; default:
		 * //acString = getResources().getString(R.string.charger_no_plugged);
		 * break;
		 * 
		 * }
		 */

	}

	private String getBatteryVoltage() {
		char[] buffer = new char[1024];
		String batteryVoltage = "";
		FileReader file = null;
		try {
			file = new FileReader("/sys/class/power_supply/usb/health");
			//dmesg /proc/kmsg     /dev/log/events   main   radio   system
			int len = file.read(buffer, 0, 1024);
			batteryVoltage = String.valueOf((new String(buffer, 0, len)));
			if (file != null) {
				file.close();
				file = null;
			}
		} catch (Exception e) {
			try {
				if (file != null) {
					file.close();
					file = null;
				}
			} catch (IOException io) {
				Log.e(TAG, "getBatteryVoltage fail");
			}
		}
		return batteryVoltage;
	}
}
