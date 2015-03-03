package com.tingken.infoshower.outside.test;

import java.io.File;
import java.util.Date;

import com.tingken.infoshower.outside.AuthResult;
import com.tingken.infoshower.outside.ServerCommand;
import com.tingken.infoshower.outside.ShowService;

public class MockShowServiceImpl implements ShowService {

	@Override
	public AuthResult authenticate(String authCode, String dimension) throws Exception {
//		AuthResult result = new AuthResult();
//		result.setAuthSuccess(true);
//		result.setShowPageAddress("http://www.sohu.com");
//		return result;
		throw new Exception("Connection failed");
	}

	@Override
	public ServerCommand heartBeat(String authCode) {
		return ServerCommand.CONNECTION_FAILED;
	}

	@Override
	public boolean uploadScreen(String authCode, Date captureTime, File capture) {
		return true;
	}

}
