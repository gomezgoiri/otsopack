package otsopack.idp.authn;

public class MetaCredentialsChecker implements ICredentialsChecker {

	private final ICredentialsChecker [] checkers;
	
	public MetaCredentialsChecker(ICredentialsChecker ... checkers){
		this.checkers = checkers;
	}
	
	@Override
	public boolean checkCredentials(String username, String password) {
		for(ICredentialsChecker checker : this.checkers)
			if(checker.checkCredentials(username, password))
				return true;
		return false;
	}

}
