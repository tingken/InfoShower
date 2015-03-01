/**
 * 
 */
package com.tingken.infoshower.core;

import com.tingken.infoshower.core.test.MockDataSource;

/**
 * @author Administrator
 * 
 */
public class DataSourceFactory {

	public static DataSource getSystemDataSource() {
		return new MockDataSource();
	}
}
