package otsopack.authn.client.exc;

public class UnexpectedAuthenticationException extends AuthenticationException {
	
	private static final long serialVersionUID = 5666510751418069577L;

	public UnexpectedAuthenticationException() {
	}

	public UnexpectedAuthenticationException(String message) {
		super(message);
	}

	public UnexpectedAuthenticationException(Throwable cause) {
		super(cause);
	}

	public UnexpectedAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
