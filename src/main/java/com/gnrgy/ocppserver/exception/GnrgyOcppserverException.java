package com.gnrgy.ocppserver.exception;

import static java.lang.String.format;

public class GnrgyOcppserverException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GnrgyOcppserverException(String message) {
        super(message);
    }

    public GnrgyOcppserverException(String message, Throwable cause) {
        super(message, cause);
    }

    // -------------------------------------------------------------------------
    // No String/variable interpolation in Java. Use format instead.
    // -------------------------------------------------------------------------

    public GnrgyOcppserverException(String template, Object arg1) {
        this(format(template, arg1));
    }

    public GnrgyOcppserverException(String template, Object arg1, Throwable cause) {
        this(format(template, arg1), cause);
    }

    public GnrgyOcppserverException(String template, Object arg1, Object arg2) {
        this(format(template, arg1, arg2));
    }

    public GnrgyOcppserverException(String template, Object arg1, Object arg2, Throwable cause) {
        this(format(template, arg1, arg2), cause);
    }
}
