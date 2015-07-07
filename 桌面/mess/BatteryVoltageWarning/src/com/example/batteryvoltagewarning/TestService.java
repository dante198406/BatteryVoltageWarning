package com.example.batteryvoltagewarning;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.io.FileReader;
import java.io.IOException;
import android.os.Handler;
import android.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.content.DialogInterface;

public class TestService extends Service {
	private static final String TAG = BatteryVoltageWarningReceiver.class
			.getSimpleName();
	private AlertDialog mDialog;
	private Handler myHandler = new Handler();
	private Runnable mTasks = new Runnable() {
		public void run() {
			String vol = "";
			vol = getBatteryVoltage();// Overvoltage Undervoltage Good
			String extra = vol.substring(0, 4);

			if ("Good".equals(extra)) {
				Log.d(TAG, "Good=================" + extra);
			} else if ("Over".equals(extra)) {
				mDialog.setTitle(com.android.internal.R.string.battery_voltage_warning_title);
				mDialog.setMessage(getString(com.android.internal.R.string.battery_over_voltage_warning_message));
				mDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
						getText(com.android.internal.R.string.ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								// myHandler.removeCallbacks(mTasks);
							}
						});
				mDialog.setCancelable(false);
				mDialog.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				mDialog.show();
				Log.d(TAG, "Overvoltage=================" + extra);
			} else if ("Unde".equals(extra)) {
				mDialog.setTitle(com.android.internal.R.string.battery_voltage_warning_title);
				mDialog.setMessage(getString(com.android.internal.R.string.battery_under_voltage_warning_message));
				mDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
						getText(com.android.internal.R.string.ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								// myHandler.removeCallbacks(mTasks);
							}
						});
				mDialog.setCancelable(false);
				mDialog.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				mDialog.show();
				Log.d(TAG, "Undervoltage=================" + extra);
			}
			myHandler.postDelayed(mTasks, 20000);
		}
	};

	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}

	@Override
	public void onCreate() {

	}

	@Override
	public void onDestroy() {
		myHandler.removeCallbacks(mTasks);
		if (mDialog.isShowing())
			mDialog.dismiss();
		Log.d(TAG, "=================" + "##Destroy");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		mDialog = new AlertDialog.Builder(getBaseContext()).create();
		myHandler.postDelayed(mTasks, 20000);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public class MyBinder extends Binder {

	}

	private String getBatteryVoltage() {
		char[] buffer = new char[1024];
		String batteryVoltage = "";
		FileReader file = null;
		try {
			file = new FileReader("/sys/class/power_supply/usb/health");
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
