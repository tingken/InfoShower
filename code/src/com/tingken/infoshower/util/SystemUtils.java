/**
 * 
 */
package com.tingken.infoshower.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author Administrator
 * 
 */
public class SystemUtils {

	/*
	 * (non-Javadoc)
	 */
	public static String getResolution(Activity window) {
		WindowManager windowManager = window.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = screenWidth = display.getWidth();
		int screenHeight = screenHeight = display.getHeight();
		return screenHeight + "*" + screenWidth;
	}

	public static String getDeviceId(Activity window) {
		WifiManager wifi = (WifiManager) window.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress();
		if (mac == null)
			return "";
		return mac.replace(":", "");
	}
}
