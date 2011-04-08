package otsopack.authn.client.exc;


public class WrongFinalRedirectException extends AuthenticationException {

	private static final long serialVersionUID = -8952503831708607733L;

	public WrongFinalRedirectException() {
	}

	public WrongFinalRedirectException(String message) {
		super(message);
	}

	public WrongFinalRedirectException(Throwable cause) {
		super(cause);
	}

	public WrongFinalRedirectException(String message, Throwable cause) {
		super(message, cause);
	}
}
