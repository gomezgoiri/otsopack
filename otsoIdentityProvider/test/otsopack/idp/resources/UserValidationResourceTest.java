package otsopack.idp.resources;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.idp.AbstractRestServerTesting;
import otsopack.idp.Controller;
import otsopack.idp.IController;
import otsopack.idp.IdpSession;
import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.authn.memory.MemoryCredentialsChecker;

public class UserValidationResourceTest  extends AbstractRestServerTesting {
	
	private final String dataProviderURIwithSecret = "http://provider/?secret=SECRET";
	private Calendar calendar;

	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		final Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("porduna",  "pablo");
		credentials.put("aigomez",  "aitor");
		credentials.put("ecastill", "eduardo");
		credentials.put("xlaiseca", "xabier");
		
		final ICredentialsChecker credentialsChecker = new MemoryCredentialsChecker(credentials);
		final IController controller = new Controller(credentialsChecker);
		this.rs.getApplication().setController(controller);
		
		this.calendar = Calendar.getInstance();
		this.calendar.set(Calendar.MILLISECOND, 0);
		this.calendar.add(Calendar.YEAR, 1);
	}
	
	@Test
	public void testValidUser() throws Exception {
		final String returned = tryWithPassword("porduna", "pablo");
		assertEquals(dataProviderURIwithSecret, returned);
	}
	
	@Test(expected=ResourceException.class)
	public void testExpiredUser() throws Exception {
		this.calendar.add(Calendar.YEAR, -5);
		final String returned = tryWithPassword("porduna", "pablo");
		assertEquals(dataProviderURIwithSecret, returned);
	}
	
	@Test(expected=ResourceException.class)
	public void testInvalidUser() throws Exception {
		tryWithPassword("porduna", "invalid.password");
	}
	
	public String tryWithPassword(String username, String password) throws Exception {
		final IdpSession session  = new IdpSession("porduna", dataProviderURIwithSecret, this.calendar);
		final String sessionId = this.rs.getApplication().getController().getSessionManager().putSession(session);

		final ClientResource cr = new ClientResource(getBaseURL() + UserValidationResource.buildUrl(sessionId));
		final Form form = new Form();
		form.set("username", username);
		form.set("password", password);
		final Representation repr = cr.post(form);
		return IOUtils.toString(repr.getStream());
	}
}
