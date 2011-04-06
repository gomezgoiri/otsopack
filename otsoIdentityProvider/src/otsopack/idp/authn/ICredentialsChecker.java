package otsopack.idp.authn;

public interface ICredentialsChecker {
	public boolean checkCredentials(String username, String password);
}
