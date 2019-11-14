package com.nirmal.wikimatch.exceptions;

public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2294884233348485830L;
	String errorReason;
	
	public String getErrorReason() {
		return errorReason;
	}

	public BaseException(String reason)
	{
		this.errorReason = reason;
	}
}
