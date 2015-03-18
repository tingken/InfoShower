/**
 * 
 */
package com.tingken.infoshower.outside;

import com.tingken.infoshower.outside.rest.ShowServiceImpl;
import com.tingken.infoshower.outside.test.MockShowServiceImpl;

/**
 * @author Administrator
 * 
 */
public class ShowServiceFactory {

	private static ShowService showService = new ShowServiceImpl();

	public static ShowService getSystemShowService() {
		return showService;
	}
}
