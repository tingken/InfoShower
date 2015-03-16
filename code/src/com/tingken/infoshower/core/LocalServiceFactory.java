/**
 * 
 */
package com.tingken.infoshower.core;

import android.content.Context;

import com.tingken.infoshower.core.sp.LocalServiceImpl;

/**
 * @author Administrator
 * 
 */
public class LocalServiceFactory {

	private static LocalService localService = null;

	public static LocalService getSystemLocalService() {
		if (localService == null) {
			localService = new LocalServiceImpl();
		}
		return localService;
	}

	public static void init(Context context) {
		LocalServiceImpl.initialize(context);
	}
}
