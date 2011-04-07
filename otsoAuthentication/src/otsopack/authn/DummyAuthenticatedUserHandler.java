package otsopack.authn;

public class DummyAuthenticatedUserHandler implements IAuthenticatedUserHandler {

	@Override
	public void onAuthenticatedUser(String userIdentifier, String redirectURI) {
		System.out.println("Valid user: " + userIdentifier + "; redirectURI: " + redirectURI);
	}

}
