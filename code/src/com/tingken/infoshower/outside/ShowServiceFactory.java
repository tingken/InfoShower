/**
 * 
 */
package com.tingken.infoshower.outside;

import com.tingken.infoshower.outside.test.MockShowServiceImpl;

/**
 * @author Administrator
 * 
 */
public class ShowServiceFactory {

	public static ShowService getSystemShowService() {
		return new MockShowServiceImpl();
	}
}
