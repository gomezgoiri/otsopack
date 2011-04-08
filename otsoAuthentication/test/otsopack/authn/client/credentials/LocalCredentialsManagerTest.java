package otsopack.authn.client.credentials;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class LocalCredentialsManagerTest {
	@Test
	public void testCredentialsSortedIncrementally(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setCredentials("http://domain/",          new Credentials("root", "root"));
		credentials.setCredentials("http://domain/sub1",      new Credentials("sub1", "root"));
		credentials.setCredentials("http://domain/sub2",      new Credentials("sub2", "root"));
		credentials.setCredentials("http://domain/sub1/sub3", new Credentials("sub3", "root"));
		
		checkValues(credentials);
	}

	@Test
	public void testCredentialsSortedDecrementally(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setCredentials("http://domain/sub1/sub3", new Credentials("sub3", "root"));
		credentials.setCredentials("http://domain/sub2",      new Credentials("sub2", "root"));
		credentials.setCredentials("http://domain/sub1",      new Credentials("sub1", "root"));
		credentials.setCredentials("http://domain/",          new Credentials("root", "root"));
		
		checkValues(credentials);
	}

	@Test
	public void testCredentialsSortedRandomly(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setCredentials("http://domain/sub2",      new Credentials("sub2", "root"));
		credentials.setCredentials("http://domain/",          new Credentials("root", "root"));
		credentials.setCredentials("http://domain/sub1/sub3", new Credentials("sub3", "root"));
		credentials.setCredentials("http://domain/sub2",      new Credentials("sub2", "root"));
		credentials.setCredentials("http://domain/sub1",      new Credentials("sub1", "root"));
		
		checkValues(credentials);
	}

	@Test
	public void testAuthenticationURIsSortedIncrementally(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setUserIdentifierURI("http://domain/",          "http://ts.otsopack.eu/u/user");
		credentials.setUserIdentifierURI("http://domain/sub1",      "http://ts.otsopack.eu/u/usersub1");
		credentials.setUserIdentifierURI("http://domain/sub2",      "http://ts.otsopack.eu/u/usersub2");
		credentials.setUserIdentifierURI("http://domain/sub1/sub3", "http://ts.otsopack.eu/u/usersub3");
		
		checkUserIdentifierValues(credentials);
	}

	@Test
	public void testAuthenticationURIsSortedDecrementally(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setUserIdentifierURI("http://domain/sub1/sub3", "http://ts.otsopack.eu/u/usersub3");
		credentials.setUserIdentifierURI("http://domain/sub2",      "http://ts.otsopack.eu/u/usersub2");
		credentials.setUserIdentifierURI("http://domain/sub1",      "http://ts.otsopack.eu/u/usersub1");
		credentials.setUserIdentifierURI("http://domain/",          "http://ts.otsopack.eu/u/user");
		
		checkUserIdentifierValues(credentials);
	}

	@Test
	public void testAuthenticationURIsSortedRandomly(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setUserIdentifierURI("http://domain/sub2",      "http://ts.otsopack.eu/u/usersub2");
		credentials.setUserIdentifierURI("http://domain/",          "http://ts.otsopack.eu/u/user");
		credentials.setUserIdentifierURI("http://domain/sub1/sub3", "http://ts.otsopack.eu/u/usersub3");
		credentials.setUserIdentifierURI("http://domain/sub2",      "http://ts.otsopack.eu/u/usersub2");
		credentials.setUserIdentifierURI("http://domain/sub1",      "http://ts.otsopack.eu/u/usersub1");
		
		checkUserIdentifierValues(credentials);
	}

	private void checkValues(final LocalCredentialsManager credentials) {
		assertEquals(new Credentials("root", "root"), credentials.getCredentials("http://domain/foo"));
		assertEquals(new Credentials("sub1", "root"), credentials.getCredentials("http://domain/sub1"));
		assertEquals(new Credentials("sub1", "root"), credentials.getCredentials("http://domain/sub1/foo"));
		assertEquals(new Credentials("sub2", "root"), credentials.getCredentials("http://domain/sub2/foo"));
		assertEquals(new Credentials("sub3", "root"), credentials.getCredentials("http://domain/sub1/sub3"));
		assertEquals(new Credentials("sub3", "root"), credentials.getCredentials("http://domain/sub1/sub3/foo"));
		
		assertEquals(null, credentials.getCredentials("http://domain2/"));
	}
	
	private void checkUserIdentifierValues(final LocalCredentialsManager credentials) {
		assertEquals("http://ts.otsopack.eu/u/user",     credentials.getUserIdentifierURI("http://domain/foo"));
		assertEquals("http://ts.otsopack.eu/u/usersub1", credentials.getUserIdentifierURI("http://domain/sub1"));
		assertEquals("http://ts.otsopack.eu/u/usersub1", credentials.getUserIdentifierURI("http://domain/sub1/foo"));
		assertEquals("http://ts.otsopack.eu/u/usersub2", credentials.getUserIdentifierURI("http://domain/sub2/foo"));
		assertEquals("http://ts.otsopack.eu/u/usersub3", credentials.getUserIdentifierURI("http://domain/sub1/sub3"));
		assertEquals("http://ts.otsopack.eu/u/usersub3", credentials.getUserIdentifierURI("http://domain/sub1/sub3/foo"));
		
		assertEquals(null, credentials.getCredentials("http://domain2/"));
	}
}
