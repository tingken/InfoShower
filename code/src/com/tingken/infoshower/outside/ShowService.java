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
	
	AuthResult authenticate(String authCode, String dimension);
	
	ServerCommand heartBeat(String authCode);
	
	boolean uploadScreen(String authCode, Date captureTime, File capture);

}
