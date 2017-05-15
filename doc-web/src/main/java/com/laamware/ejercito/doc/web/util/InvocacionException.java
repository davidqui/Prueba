package com.laamware.ejercito.doc.web.util;

public class InvocacionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvocacionException() {
		super();
	}

	public InvocacionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvocacionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvocacionException(String message) {
		super(message);
	}

	public InvocacionException(Throwable cause) {
		super(cause);
	}

}
