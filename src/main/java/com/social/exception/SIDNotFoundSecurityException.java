package com.social.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SIDNotFoundSecurityException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 11L;

	public SIDNotFoundSecurityException() {
        super();
    }

    public SIDNotFoundSecurityException(String message) {
        super(message);
    }

    public SIDNotFoundSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
