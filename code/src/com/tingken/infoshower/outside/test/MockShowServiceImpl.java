package com.tingken.infoshower.outside.test;

import java.io.File;
import java.util.Date;

import com.tingken.infoshower.outside.AuthResult;
import com.tingken.infoshower.outside.ServerCommand;
import com.tingken.infoshower.outside.ShowService;
import com.tingken.infoshower.outside.VersionInfo;

public class MockShowServiceImpl implements ShowService {

	@Override
	public AuthResult authenticate(String authCode, String deviceId, String dimension) throws Exception {
		AuthResult result = new AuthResult();
		result.setAuthSuccess(true);
		result.setLoginId("2fs32sdf23");
		result.setShowPageAddress("http://www.sohu.com");
		return result;
		// throw new Exception("Connection failed");
	}

	@Override
	public ServerCommand heartBeat(String authCode) {
		return ServerCommand.NONE;
	}

	@Override
	public boolean uploadScreen(String authCode, Date captureTime, File capture) {
		return true;
	}

	@Override
	public void init(String basicUrl) {
		// TODO Auto-generated method stub

	}

	@Override
	public VersionInfo getLatestVersion(String loginId) {
		// TODO Auto-generated method stub
		return null;
	}

}
