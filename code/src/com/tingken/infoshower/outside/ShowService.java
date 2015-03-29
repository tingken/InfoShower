/**
 * 
 */
package com.tingken.infoshower.outside;

import java.io.File;
import java.util.Date;

/**
 * @author tingken.com
 * 
 */
public interface ShowService {
	public static final String DEFAULT_SERVER_ADDRESS = "http://125.71.200.138:10001/ii/";

	void init(String basicUrl);

	AuthResult authenticate(String authCode, String deviceId, String dimension) throws Exception;

	ServerCommand heartBeat(String loginId);

	boolean uploadScreen(String loginId, Date captureTime, File capture);

	VersionInfo getLatestVersion(String loginId);

}
