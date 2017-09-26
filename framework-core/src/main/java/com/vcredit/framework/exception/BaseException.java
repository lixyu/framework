package com.vcredit.framework.exception;


/**
 * 异常基类
 * @author kezhan
 *
 */
public class BaseException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3485125636434778859L;
	
	public BaseException(String errorMessage) {
		super(errorMessage);
	}
}
