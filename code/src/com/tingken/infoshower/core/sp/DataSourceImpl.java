/**
 * 
 */
package com.tingken.infoshower.core.sp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.tingken.infoshower.core.DataSource;

/**
 * @author Administrator
 * 
 */
public class DataSourceImpl implements DataSource {
	private static final String TAG = "effort";

	private static final String[] ACCOUNT_COLUMN = { "REG_NUM", "CACHED_SERVER_ADDRESS", "OFFLINE_SERVER_PAGE" };
	private static Activity mContext;

	private SharedPreferences sp;

	public static void initialize(Activity context) {
		if (mContext == null) {
			mContext = context;
		}
	}

	public DataSourceImpl() {
		sp = mContext.getSharedPreferences("SP", Context.MODE_PRIVATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tingken.infoshower.core.DataSource#getAuthCode()
	 */
	@Override
	public String getAuthCode() {
		return sp.getString(ACCOUNT_COLUMN[0], "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tingken.infoshower.core.DataSource#getResolution()
	 */
	@Override
	public String getResolution() {
		WindowManager windowManager = mContext.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = screenWidth = display.getWidth();
		int screenHeight = screenHeight = display.getHeight();
		return screenHeight + "*" + screenWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tingken.infoshower.core.DataSource#getCachedServerAddress()
	 */
	@Override
	public String getCachedServerAddress() {
		return sp.getString(ACCOUNT_COLUMN[1], "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tingken.infoshower.core.DataSource#getOfflineServerPage()
	 */
	@Override
	public String getOfflineServerPage() {
		return sp.getString(ACCOUNT_COLUMN[2], "");
	}

}
