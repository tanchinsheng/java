/**
 * 
 */
package com.st.mcc.rtm.util.exception;

import org.apache.log4j.Logger;

/**
 * @author hdle
 *
 */
public class RTMException extends Exception {
	
	private int errorCode = 0;
	private String errorText = "";
	
	public RTMException() {
		errorCode = 0;
		errorText = "";
	}
	
	public String toString() {
		return ("Error " + errorCode + " - " + errorText);
	}
	
	public void reportError(int code, String text, Logger _logger) {
		this.setErrorCode(code);
		this.setErrorText(text);
		_logger.error(text);
	}

	/**
	 * @return Returns the errorCode.
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode The errorCode to set.
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return Returns the errorText.
	 */
	public String getErrorText() {
		return errorText;
	}

	/**
	 * @param errorText The errorText to set.
	 */
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

}
