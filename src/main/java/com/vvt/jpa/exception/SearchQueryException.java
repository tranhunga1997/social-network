package com.vvt.jpa.exception;

public class SearchQueryException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearchQueryException(String msg) {
		super(msg);
	}
	
	public SearchQueryException(String msg, Throwable throwable) {
		super(msg, throwable);
		
	}

}
