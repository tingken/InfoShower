package com.tingken.infoshower.core.test;

import com.tingken.infoshower.core.DataSource;

public class MockDataSource implements DataSource {

	@Override
	public String getAuthCode() {
		return "authCode:001";
	}

	@Override
	public String getResolution() {
		return "1920*1080";
	}

	@Override
	public String getCachedServerAddress() {
		return "www.sohu.com";
	}

	@Override
	public String getOfflineServerPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
