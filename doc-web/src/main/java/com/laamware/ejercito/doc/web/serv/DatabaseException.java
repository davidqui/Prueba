package com.laamware.ejercito.doc.web.serv;

public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Código de la excepción en la base de datos
	 */
	private Integer code;

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public DatabaseException() {
		super();
	}

	public DatabaseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable cause) {
		super(cause);
	}

}
