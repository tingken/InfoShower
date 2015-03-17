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
	private String loginId;
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

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
}
