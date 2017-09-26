package com.vcredit.framework.redis;

import org.springframework.dao.DataAccessException;

public class RedisTokenException extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7404104911206065688L;

	public RedisTokenException(String errorMessage) {
		super(errorMessage);
	}

}
