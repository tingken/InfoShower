/**
 * 
 */
package com.tingken.infoshower.core.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tingken.infoshower.core.LocalService;

/**
 * @author Administrator
 * 
 */
public class LocalServiceImpl implements LocalService {
	private static final String TAG = "effort";

	private static final String REG_NUM = "REG_NUM";
	private static final String CACHED_SERVER_ADDRESS = "CACHED_SERVER_ADDRESS";
	private static final String OFFLINE_SERVER_PAGE = "OFFLINE_SERVER_PAGE";
	private static final String AUTO_START = "AUTO_START";
	private static final String SHOW_SERVER_ADDRESS = "SHOW_SERVER_ADDRESS";

	private static final String[] ACCOUNT_COLUMN = { "REG_NUM", "CACHED_SERVER_ADDRESS", "OFFLINE_SERVER_PAGE" };
	private static Context mContext;
	private static String loginId;

	private SharedPreferences sp;

	public static void initialize(Context context) {
		if (mContext == null) {
			mContext = context;
		}
	}

	public LocalServiceImpl() {
		sp = mContext.getSharedPreferences("SP", Context.MODE_PRIVATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tingken.infoshower.core.DataSource#getAuthCode()
	 */
	@Override
	public String getAuthCode() {
		return sp.getString(REG_NUM, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tingken.infoshower.core.DataSource#getCachedServerAddress()
	 */
	@Override
	public String getCachedServerAddress() {
		return sp.getString(CACHED_SERVER_ADDRESS, "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tingken.infoshower.core.DataSource#getOfflineServerPage()
	 */
	@Override
	public String getOfflineServerPage() {
		return sp.getString(OFFLINE_SERVER_PAGE, "");
	}

	@Override
	public void saveAuthCode(String regNum) {
		Editor editor = sp.edit();
		editor.putString(REG_NUM, regNum);
		editor.commit();
	}

	@Override
	public void saveCachedServerAddress(String url) {
		Editor editor = sp.edit();
		editor.putString(CACHED_SERVER_ADDRESS, url);
		editor.commit();
	}

	@Override
	public void saveOfflineServerPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAutoStart() {
		return sp.getBoolean(AUTO_START, true);
	}

	@Override
	public void setAutoStart(boolean autoStart) {
		Editor editor = sp.edit();
		editor.putBoolean(AUTO_START, autoStart);
		editor.commit();
	}

	@Override
	public String getShowServiceAddress() {
		return sp.getString(SHOW_SERVER_ADDRESS, null);
	}

	@Override
	public void saveShowServiceAddress(String urlPrefix) {
		Editor editor = sp.edit();
		editor.putString(SHOW_SERVER_ADDRESS, urlPrefix);
		editor.commit();
	}

	@Override
	public void saveLoginId(String loginId) {
		LocalServiceImpl.loginId = loginId;
	}

	@Override
	public String getLoginId() {
		return LocalServiceImpl.loginId;
	}

}
