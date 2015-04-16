/**
 * 
 */
package com.tingken.infoshower.outside.rest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.tingken.infoshower.outside.AuthResult;
import com.tingken.infoshower.outside.ServerCommand;
import com.tingken.infoshower.outside.ShowService;
import com.tingken.infoshower.outside.VersionInfo;
import com.tingken.infoshower.util.UploadUtils;

/**
 * @author tingken.com
 * 
 */
public class ShowServiceImpl implements ShowService {

	private static TreeMap<String, ServerCommand> commandIndex = new TreeMap<String, ServerCommand>();
	static {
		for (ServerCommand command : ServerCommand.values()) {
			commandIndex.put(command.toString(), command);
		}
	}

	private static String serverAddress = DEFAULT_SERVER_ADDRESS;
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
		String url = serverAddress + "VideoDisplay.svc/DeviceWeb/Authenticate?regNum="
		        + URLEncoder.encode(authCode, "utf-8") + "&dimension=" + URLEncoder.encode(dimension, "utf-8");
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
		String url;
		try {
			url = serverAddress + "VideoDisplay.svc/DeviceWeb/Login/" + URLEncoder.encode(loginId, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			return ServerCommand.NONE;
		}
		ServerCommand result = null;
		try {
			HttpResponse response = restServiceWorker.getResponse(url);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = ServerCommand.NONE;
				HttpEntity entity = response.getEntity();
				String out = null;
				if (entity != null) {
					out = EntityUtils.toString(entity, "UTF-8");
				}
				JSONObject jsonObject = new JSONObject(out);
				ServerCommand command = commandIndex.get(jsonObject.getString("command"));
				if (command != null) {
					result = command;
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
		String url;
		try {
			url = serverAddress + "IPrintScreen.aspx?loginId=" + URLEncoder.encode(loginId, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		// return restServiceWorker.postImage(capture, url);
		return UploadUtils.uploadFile(capture, url);
	}

	@Override
	public void init(String basicUrl) {
		serverAddress = basicUrl;
	}

	@Override
	public VersionInfo getLatestVersion(String loginId) {
		String url;
		try {
			url = serverAddress + "VideoDisplay.svc/DeviceWeb/latestVersion/" + URLEncoder.encode(loginId, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			return null;
		}
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
