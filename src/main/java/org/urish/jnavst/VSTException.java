package org.urish.jnavst;

public class VSTException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public VSTException() {
		super();
	}

	public VSTException(String message, Throwable cause) {
		super(message, cause);
	}

	public VSTException(String message) {
		super(message);
	}

	public VSTException(Throwable cause) {
		super(cause);
	}
}
