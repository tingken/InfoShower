/**
 * 
 */
package com.tingken.infoshower.core;

/**
 * @author Administrator
 * 
 */
public interface LocalService {
	String getAuthCode();

	String getCachedServerAddress();

	String getShowServiceAddress();

	String getOfflineServerPage();

	void saveAuthCode(String regNum);

	void saveCachedServerAddress(String url);

	void saveOfflineServerPage();

	boolean isAutoStart();

	void setAutoStart(boolean autoStart);

	void saveShowServiceAddress(String urlPrefix);

	void saveLoginId(String loginId);

	String getLoginId();
}
