package otsopack.idp.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import otsopack.idp.sessions.Session;

public class UserResourceTest  extends AbstractRestServerTesting {

	@Before
	public void setUp() throws Exception{
		super.setUp();
		
		final ICredentialsChecker credentialsChecker = new MemoryCredentialsChecker(null);
		final IController controller = new Controller(credentialsChecker);
		this.rs.getApplication().setController(controller);
	}
	
	@Test
	public void testCorrectURL() throws Exception {
		final String uri = getBaseURL() + UserResource.ROOT.replace("{user}", "porduna");
		final ClientResource cr = new ClientResource(uri);
		final SimpleDateFormat dateFormat = new SimpleDateFormat(UserResource.DATE_FORMAT);
		
		final Calendar futureDate = Calendar.getInstance();
		futureDate.set(Calendar.MILLISECOND, 0);
		futureDate.add(Calendar.YEAR, 1);
		final String dataProviderURIwithSecret = "http://provider/?secret=MYSECRET";
		
		final Form form = new Form();
		form.set(UserResource.DATA_PROVIDER_URI_WITH_SECRET_NAME, dataProviderURIwithSecret);
		form.set(UserResource.EXPIRATION_NAME, dateFormat.format(futureDate.getTime())); 
		
		final Representation repr = cr.post(form);
		final String response = IOUtils.toString(repr.getStream());
		
		// It returned a sessionId
		final String baseResponse = getBaseURL() + UserValidationResource.buildUrl("");
		assertThat(response, containsString(baseResponse));
		final String idpSessionId = response.substring(baseResponse.length());
		
		// We have that session stored
		final Session session = this.rs.getApplication().getController().getSessionManager().getSession(idpSessionId);
		assertNotNull(session);
		
		assertEquals(dataProviderURIwithSecret, session.getDataProviderURIwithSecret());
		assertEquals(futureDate.getTimeInMillis(), session.getExpirationDate().getTimeInMillis());
	}
}
