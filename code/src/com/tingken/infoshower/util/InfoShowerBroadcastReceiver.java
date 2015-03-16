package com.tingken.infoshower.util;

import com.tingken.infoshower.core.LocalService;
import com.tingken.infoshower.core.LocalServiceFactory;
import com.tingken.infoshower.view.WelcomActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class InfoShowerBroadcastReceiver extends BroadcastReceiver {
	public static final String TAG = "effort";

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			LocalServiceFactory.init(context);
			LocalService localService = LocalServiceFactory.getSystemLocalService();
			if (localService.isAutoStart()) {
				Intent i = new Intent(context, WelcomActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
		} else {
			Toast.makeText(context, "Received unexpected intent " + intent.toString(), Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Received unexpected intent " + intent.toString());
		}
	}
}
