package otsopack.authn.client.exc;

public class RedirectedToWrongIdentityProviderException extends
		AuthenticationException {

	private static final long serialVersionUID = 1665524896457211085L;

	public RedirectedToWrongIdentityProviderException() {
	}

	public RedirectedToWrongIdentityProviderException(String message) {
		super(message);
	}

	public RedirectedToWrongIdentityProviderException(Throwable cause) {
		super(cause);
	}

	public RedirectedToWrongIdentityProviderException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
