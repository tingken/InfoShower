/**
 * 
 */
package com.tingken.infoshower.outside;

/**
 * @author tingken.com
 *
 */
public class AuthResult {

	private boolean authSuccess;
	private String showPageAddress;
	public boolean isAuthSuccess() {
		return authSuccess;
	}
	public void setAuthSuccess(boolean authSuccess) {
		this.authSuccess = authSuccess;
	}
	public String getShowPageAddress() {
		return showPageAddress;
	}
	public void setShowPageAddress(String showPageAddress) {
		this.showPageAddress = showPageAddress;
	}
}
