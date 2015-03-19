/**
 * 
 */
package com.tingken.infoshower.outside.rest;

import java.io.File;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.tingken.infoshower.outside.AuthResult;
import com.tingken.infoshower.outside.ServerCommand;
import com.tingken.infoshower.outside.ShowService;
import com.tingken.infoshower.outside.VersionInfo;

/**
 * @author tingken.com
 * 
 */
public class ShowServiceImpl implements ShowService {

	private static String serverAddress = "http://192.168.2.8:8080/showService-war-1.0/showService/";
	private HttpServiceWorker restServiceWorker = new HttpServiceWorker();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tingken.infoshower.outside.ShowService#authenticate(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AuthResult authenticate(String authCode, String deviceId, String dimension) throws Exception {
		String url = serverAddress + "authenticate?regNum=" + authCode + "&dimension=" + dimension;
		AuthResult result = null;
		HttpResponse response = restServiceWorker.getResponse(url);
		result = new AuthResult();
		result.setAuthSuccess(false);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String out = null;
			if (entity != null) {
				out = EntityUtils.toString(entity, "UTF-8");
			}
			JSONObject jsonObject = new JSONObject(out);
			result.setAuthSuccess(true);
			result.setLoginId(jsonObject.getString("loginId"));
			result.setShowPageAddress(jsonObject.getString("showPageAddress"));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tingken.infoshower.outside.ShowService#heartBeat(java.lang.String)
	 */
	@Override
	public ServerCommand heartBeat(String loginId) {
		String url = serverAddress + "heartBeat?loginId=" + loginId;
		ServerCommand result = null;
		try {
			HttpResponse response = restServiceWorker.getResponse(url);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String out = null;
				if (entity != null) {
					out = EntityUtils.toString(entity, "UTF-8");
				}
				JSONObject jsonObject = new JSONObject(out);
				String command = jsonObject.getString("command");
				if ("null".equals(command)) {
					result = ServerCommand.NONE;
				} else {
					result = ServerCommand.valueOf(command);
				}
			} else {
				result = ServerCommand.NONE;
			}
		} catch (IllegalArgumentException e) {
			result = ServerCommand.NONE;
		} catch (Exception e) {
			result = ServerCommand.CONNECTION_FAILED;
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tingken.infoshower.outside.ShowService#uploadScreen(java.lang.String,
	 * java.util.Date, java.io.File)
	 */
	@Override
	public boolean uploadScreen(String loginId, Date captureTime, File capture) {
		String url = serverAddress + "uploadCapture?loginId=" + loginId;
		return restServiceWorker.postImage(capture, url);
	}

	@Override
	public void init(String basicUrl) {
		serverAddress = basicUrl;
	}

	@Override
	public VersionInfo getLatestVersion() {
		String url = serverAddress + "latestVersion";
		VersionInfo result = null;
		try {
			String responseEntity = restServiceWorker.executeGet(url);
			JSONObject jsonObject = new JSONObject(responseEntity);
			result = new VersionInfo();
			result.setVersionCode(jsonObject.getInt("versionCode"));
			result.setVersionName(jsonObject.getString("versionName"));
			result.setDownloadAddress(jsonObject.getString("downloadAddress"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
