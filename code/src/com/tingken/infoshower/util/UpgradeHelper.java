package com.tingken.infoshower.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class UpgradeHelper {

	/**
	 * retrieve package version
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// get version code
			versionCode = context.getPackageManager().getPackageInfo("com.szy.update", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

}
