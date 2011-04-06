package otsopack.idp.resources;

import static org.junit.Assert.assertEquals;

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

import otsopack.idp.AbstractRestServerTesting;
import otsopack.idp.Controller;
import otsopack.idp.IController;
import otsopack.idp.authn.ICredentialsChecker;
import otsopack.idp.authn.memory.MemoryCredentialsChecker;

public class IdpIntegrationTest  extends AbstractRestServerTesting {

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
	public void testIntegration() throws Exception {
		// Here we're the data provider
		final ClientResource dataProviderClientResource = new ClientResource(getBaseURL() + UserResource.ROOT.replace("{user}", "porduna"));
		final SimpleDateFormat dateFormat = new SimpleDateFormat(UserResource.DATE_FORMAT);
		
		final Calendar futureDate = Calendar.getInstance();
		futureDate.set(Calendar.MILLISECOND, 0);
		futureDate.add(Calendar.YEAR, 1);
		System.out.println(dateFormat.format(futureDate.getTime()));
		final String dataProviderURIwithSecret = "http://provider/?secret=MYSECRET";
		
		final Form dataProviderForm = new Form();
		dataProviderForm.set(UserResource.DATA_PROVIDER_URI_WITH_SECRET_NAME, dataProviderURIwithSecret);
		dataProviderForm.set(UserResource.EXPIRATION_NAME, dateFormat.format(futureDate.getTime())); 
		
		final Representation repr = dataProviderClientResource.post(dataProviderForm);
		final String responseToDataProvider = IOUtils.toString(repr.getStream());
		
		// But here we're the user
		System.out.println(responseToDataProvider);
		final ClientResource userClientResource = new ClientResource(responseToDataProvider);
		final Form userForm = new Form();
		userForm.set("username", "porduna");
		userForm.set("password", "pablo");
		final Representation userResponseRepresentation = userClientResource.post(userForm);
		
		assertEquals(dataProviderURIwithSecret, IOUtils.toString(userResponseRepresentation.getStream()));
	}
}
