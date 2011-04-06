package otsopack.idp.resources;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import otsopack.idp.AbstractRestServerTesting;
import otsopack.idp.Controller;
import otsopack.idp.IController;
import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.authn.memory.MemoryCredentialsChecker;

public class UserValidationResourceTest  extends AbstractRestServerTesting {

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
	}
	
	@Test
	public void testValidUser() throws Exception {
		final ClientResource cr = new ClientResource(getBaseURL() + "users/validations");
		final Form form = new Form();
		form.set("username", "porduna");
		form.set("password", "pablo");
		final Representation repr = cr.post(form);
		final String response = IOUtils.toString(repr.getStream());
		assertEquals("true", response);
	}

	@Test
	public void testInvalidUser() throws Exception {
		final ClientResource cr = new ClientResource(getBaseURL() + "users/validations");
		final Form form = new Form();
		form.set("username", "porduna");
		form.set("password", "invalid");
		final Representation repr = cr.post(form);
		final String response = IOUtils.toString(repr.getStream());
		assertEquals("false", response);
	}

}
