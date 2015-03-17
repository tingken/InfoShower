package com.tingken.infoshower.outside.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpServiceWorker {
	// private String url =
	// "http://192.168.0.17:8080/EvaluationService/testLogin/login!json.action";
	private HttpClient client;
	private HttpGet getRequest;
	private HttpPost postRequest;
	private HttpResponse response;
	private String TAG = "effort";

	public HttpServiceWorker() {
		getRequest = new HttpGet();
		postRequest = new HttpPost();
	}

	public void open() {
		if (client == null) {
			client = new DefaultHttpClient();
		}
	}

	public String executeGet(String url) throws Exception {
		// BufferedReader in = null;
		String out = null;
		// 定义HttpClient
		// HttpClient client = new DefaultHttpClient();
		// 实例化HTTP方法
		if (getContent(url)) {
			// getRequest.setURI(new URI(url));
			// response = client.execute(getRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				out = EntityUtils.toString(entity, "UTF-8");
			}
		}
		return out;
	}

	public HttpResponse getResponse(String url) throws Exception {
		open();
		getRequest.setURI(new URI(url));
		response = client.execute(getRequest);
		return response;
	}

	public InputStream executeGetStream(String url) throws Exception {
		// BufferedReader in = null;
		open();
		InputStream is = null;
		// 定义HttpClient
		// 实例化HTTP方法
		getRequest.setURI(new URI(url));
		response = client.execute(getRequest);
		is = response.getEntity().getContent();
		// close();
		return is;
	}

	public String getValue(String url) {
		if (getContent(url)) {
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					return out;
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return null;
	}

	public boolean getContent(String url) {
		try {
			open();
			getRequest.setURI(new URI(url));
			response = client.execute(getRequest);
			Log.e(TAG, url);
			int code = response.getStatusLine().getStatusCode();
			Log.e(TAG, "isConnect(),response status code:" + code);
			if (code == 200) { // 200 means OK
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
		// close();
		return false;
	}

	public void addFile(String key, String filePath, String type) {
		File file = new File(filePath);
		ContentBody cbFileData = new FileBody(file, type);
		// postEntity.addPart(key, cbFileData);
	}

	public boolean postData(Map<String, String> data, String url) {
		ContentBody cb = null;
		MultipartEntity postEntity = new MultipartEntity();
		try {
			// Set<String> set = data.keySet();
			// for(String key : set){
			// cb = new StringBody(data.get(key));
			// postEntity.addPart(key, cb);
			// }
			open();
			postRequest.setURI(new URI(url));
			Log.e(TAG, url);
			// request.setEntity(postEntity);
			response = client.execute(postRequest);
			int code = response.getStatusLine().getStatusCode();
			Log.e(TAG, "postData(),response status code:" + code);
			if (code == 201) { // 200表示请求成功
				return true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		// close();
		return false;
	}

	public boolean postImage(String filePath, String url) {
		ContentBody cb = null;
		MultipartEntity postEntity = new MultipartEntity();
		try {
			File file = new File(filePath);
			ContentBody cbFileData = new FileBody(file);
			postEntity.addPart("image", cbFileData);
			open();
			postRequest.setURI(new URI(url));
			postRequest.setEntity(postEntity);
			Log.e(TAG, url);
			// request.setEntity(postEntity);
			response = client.execute(postRequest);
			int code = response.getStatusLine().getStatusCode();
			Log.e(TAG, "postData(),response status code:" + code);
			if (code / 200 == 1) {
				return true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		// close();
		return false;
	}

	public boolean postImage(File file, String url) {
		ContentBody cb = null;
		MultipartEntity postEntity = new MultipartEntity();
		try {
			ContentBody cbFileData = new FileBody(file);
			postEntity.addPart("image", cbFileData);
			open();
			postRequest.setURI(new URI(url));
			postRequest.setEntity(postEntity);
			Log.e(TAG, url);
			// request.setEntity(postEntity);
			response = client.execute(postRequest);
			int code = response.getStatusLine().getStatusCode();
			Log.e(TAG, "postData(),response status code:" + code);
			if (code / 200 == 1) {
				return true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		// close();
		return false;
	}

	public boolean getContent() {
		try {
			InputStream reader = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void close() {
		if (client != null) {
			client.getConnectionManager().shutdown();
			client = null;
		}
	}
}
