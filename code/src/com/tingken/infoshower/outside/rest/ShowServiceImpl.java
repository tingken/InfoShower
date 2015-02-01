/**
 * 
 */
package com.tingken.infoshower.outside.rest;

import java.io.File;
import java.util.Date;

import com.tingken.infoshower.outside.AuthResult;
import com.tingken.infoshower.outside.ServerCommand;
import com.tingken.infoshower.outside.ShowService;

/**
 * @author tingken.com
 *
 */
public class ShowServiceImpl implements ShowService {

	/* (non-Javadoc)
	 * @see com.tingken.infoshower.outside.ShowService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public AuthResult authenticate(String authCode, String dimension) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tingken.infoshower.outside.ShowService#heartBeat(java.lang.String)
	 */
	@Override
	public ServerCommand heartBeat(String authCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.tingken.infoshower.outside.ShowService#uploadScreen(java.lang.String, java.util.Date, java.io.File)
	 */
	@Override
	public boolean uploadScreen(String authCode, Date captureTime, File capture) {
		// TODO Auto-generated method stub
		return false;
	}

}
