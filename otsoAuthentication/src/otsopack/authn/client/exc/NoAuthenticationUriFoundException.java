package otsopack.authn.client.exc;

public class NoAuthenticationUriFoundException extends AuthenticationException {

	private static final long serialVersionUID = -1154153339372917683L;

	public NoAuthenticationUriFoundException() {
	}

	public NoAuthenticationUriFoundException(String message) {
		super(message);
	}

	public NoAuthenticationUriFoundException(Throwable cause) {
		super(cause);
	}

	public NoAuthenticationUriFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
