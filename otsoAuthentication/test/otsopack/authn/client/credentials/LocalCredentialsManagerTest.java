package otsopack.authn.client.credentials;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class LocalCredentialsManagerTest {
	@Test
	public void testSortedIncrementally(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setCredentials("http://domain/",          new Credentials("root", "root"));
		credentials.setCredentials("http://domain/sub1",      new Credentials("sub1", "root"));
		credentials.setCredentials("http://domain/sub2",      new Credentials("sub2", "root"));
		credentials.setCredentials("http://domain/sub1/sub3", new Credentials("sub3", "root"));
		
		checkValues(credentials);
	}

	@Test
	public void testSortedDecrementally(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setCredentials("http://domain/sub1/sub3", new Credentials("sub3", "root"));
		credentials.setCredentials("http://domain/sub2",      new Credentials("sub2", "root"));
		credentials.setCredentials("http://domain/sub1",      new Credentials("sub1", "root"));
		credentials.setCredentials("http://domain/",          new Credentials("root", "root"));
		
		checkValues(credentials);
	}

	@Test
	public void testSortedRandomly(){
		final LocalCredentialsManager credentials = new LocalCredentialsManager();
		
		credentials.setCredentials("http://domain/sub2",      new Credentials("sub2", "root"));
		credentials.setCredentials("http://domain/",          new Credentials("root", "root"));
		credentials.setCredentials("http://domain/sub1/sub3", new Credentials("sub3", "root"));
		credentials.setCredentials("http://domain/sub2",      new Credentials("sub2", "root"));
		credentials.setCredentials("http://domain/sub1",      new Credentials("sub1", "root"));
		
		checkValues(credentials);
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
}
