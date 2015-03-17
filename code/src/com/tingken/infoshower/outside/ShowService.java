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

	void init(String basicUrl);

	AuthResult authenticate(String authCode, String deviceId, String dimension) throws Exception;

	ServerCommand heartBeat(String loginId);

	boolean uploadScreen(String authCode, Date captureTime, File capture);

	VersionInfo getLatestVersion();

}
