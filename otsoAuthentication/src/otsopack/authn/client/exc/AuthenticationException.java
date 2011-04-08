package otsopack.authn.client.exc;

public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 4344808631266594396L;

	public AuthenticationException() {
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
