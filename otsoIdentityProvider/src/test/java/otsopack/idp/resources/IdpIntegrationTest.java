package otsopack.idp.resources;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import otsopack.idp.IIdpController;
import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.authn.memory.MemoryCredentialsChecker;

public class IdpIntegrationTest  extends AbstractRestServerTesting {

	private final String dataProviderURIwithSecret = "http://provider/?secret=MYSECRET";
	
	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		final Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("porduna",  "pablo");
		credentials.put("aigomez",  "aitor");
		credentials.put("ecastill", "eduardo");
		credentials.put("xlaiseca", "xabier");
		
		final ICredentialsChecker credentialsChecker = new MemoryCredentialsChecker(credentials);
		final IIdpController controller = new Controller(credentialsChecker);
		this.rs.getApplication().setController(controller);
	}
	
	@Test
	public void testValidIntegration() throws Exception {
		// Here we're the data provider
		final String responseToDataProvider = actAsDataProvider("porduna");
		
		// But here we're the user
		actAsUser(responseToDataProvider, "porduna", "pablo");
	}
	
	@Test(expected=ResourceException.class)
	public void testMaliciousIntegration() throws Exception {
		// We say the data server that we're porduna
		final String responseToDataProvider = actAsDataProvider("porduna");
		
		// But then we authenticate with other valid user
		actAsUser(responseToDataProvider, "xlaiseca", "xabier");
		
		// An exception is expected!
	}
	
	private String actAsDataProvider(String username) throws IOException {
		final ClientResource dataProviderClientResource = new ClientResource(getBaseURL() + UserResource.ROOT.replace("{user}", username));
		final SimpleDateFormat dateFormat = new SimpleDateFormat(UserResource.DATE_FORMAT);
		
		final Calendar futureDate = Calendar.getInstance();
		futureDate.set(Calendar.MILLISECOND, 0);
		futureDate.add(Calendar.YEAR, 1);
		
		final Form dataProviderForm = new Form();
		dataProviderForm.set(UserResource.DATA_PROVIDER_URI_WITH_SECRET_NAME, this.dataProviderURIwithSecret);
		dataProviderForm.set(UserResource.EXPIRATION_NAME, dateFormat.format(futureDate.getTime())); 
		
		final Representation repr = dataProviderClientResource.post(dataProviderForm);
		final String responseToDataProvider = IOUtils.toString(repr.getStream());
		return responseToDataProvider;
	}
	
	private void actAsUser(String responseToDataProvider, String username, String password) throws Exception {
		final ClientResource userClientResource = new ClientResource(responseToDataProvider);
		final Form userForm = new Form();
		userForm.set("username", username);
		userForm.set("password", password);
		final Representation userResponseRepresentation = userClientResource.post(userForm);
		
		assertEquals(this.dataProviderURIwithSecret, IOUtils.toString(userResponseRepresentation.getStream()));
	}
}
