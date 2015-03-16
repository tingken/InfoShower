/**
 * 
 */
package com.tingken.infoshower.util;

import android.app.Activity;
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
}
