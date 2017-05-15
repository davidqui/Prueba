package com.laamware.ejercito.doc.web.util;

public class SintaxisException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SintaxisException() {
		super();
	}

	public SintaxisException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SintaxisException(String message, Throwable cause) {
		super(message, cause);
	}

	public SintaxisException(String message) {
		super(message);
	}

	public SintaxisException(Throwable cause) {
		super(cause);
	}

}
