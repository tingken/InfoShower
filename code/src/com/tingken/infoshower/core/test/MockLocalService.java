package com.tingken.infoshower.core.test;

import com.tingken.infoshower.core.LocalService;

public class MockLocalService implements LocalService {

	boolean mAutoStart = true;

	@Override
	public String getAuthCode() {
		return null;// "authCode:001";
	}

	@Override
	public String getCachedServerAddress() {
		return "http://www.sohu.com";
	}

	@Override
	public String getOfflineServerPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveAuthCode(String regNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCachedServerAddress(String url) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveOfflineServerPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAutoStart() {
		return mAutoStart;
	}

	@Override
	public void setAutoStart(boolean autoStart) {
		mAutoStart = autoStart;
	}

	@Override
	public String getShowServiceAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveShowServiceAddress(String urlPrefix) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveLoginId(String loginId) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getLoginId() {
		// TODO Auto-generated method stub
		return null;
	}

}
