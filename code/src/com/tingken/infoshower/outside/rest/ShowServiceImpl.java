/**
 * 
 */
package com.tingken.infoshower.outside.rest;

import java.io.File;
import java.util.Date;

import org.json.JSONObject;

import com.tingken.infoshower.outside.AuthResult;
import com.tingken.infoshower.outside.ServerCommand;
import com.tingken.infoshower.outside.ShowService;

/**
 * @author tingken.com
 *
 */
public class ShowServiceImpl implements ShowService {

	private static final String SERVER_ADDRESS = "http://127.0.0.1:8080/ShowService/";
	private RestServiceWorker restServiceWorker = new RestServiceWorker();

	/* (non-Javadoc)
	 * @see com.tingken.infoshower.outside.ShowService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public AuthResult authenticate(String authCode, String dimension) {
		String url = SERVER_ADDRESS + "auth?code=" + authCode + "&dimension=" + dimension;
		AuthResult result = null;
		try {
			String responseEntity = restServiceWorker.executeGet(url);
			JSONObject jsonObject = new JSONObject(responseEntity);
			result = new AuthResult();
			result.setAuthSuccess(jsonObject.getBoolean("authSuccess"));
			result.setShowPageAddress(jsonObject.getString("showPageAddress"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.tingken.infoshower.outside.ShowService#heartBeat(java.lang.String)
	 */
	@Override
	public ServerCommand heartBeat(String authCode) {
		String url = SERVER_ADDRESS + "heartBeat?code=" + authCode;
		ServerCommand result = null;
		try {
			String responseEntity = restServiceWorker.executeGet(url);
			JSONObject jsonObject = new JSONObject(responseEntity);
			result = ServerCommand.valueOf(jsonObject.getString("serverCommand"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.tingken.infoshower.outside.ShowService#uploadScreen(java.lang.String, java.util.Date, java.io.File)
	 */
	@Override
	public boolean uploadScreen(String authCode, Date captureTime, File capture) {
		return true;
	}

}
