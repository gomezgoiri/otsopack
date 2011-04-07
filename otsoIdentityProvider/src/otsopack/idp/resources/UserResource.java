package otsopack.idp.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import otsopack.idp.Session;

public class UserResource extends AbstractOtsoServerResource implements IUserResource {

	public static final String EXPIRATION_NAME = "expiration";
	public static final String DATA_PROVIDER_URI_WITH_SECRET_NAME = "dataProviderURIwithSecret";
	public static String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
	public static String ROOT = "/users/u/{user}";
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, UserResource.class);
		return roots;
	}

	@Override
	public Representation postUserResource(Representation entity) {
		final Form form = new Form(entity);
		final String dataProviderURI = form.getFirstValue(DATA_PROVIDER_URI_WITH_SECRET_NAME);
		final String expirationStr   = form.getFirstValue(EXPIRATION_NAME);
		
		Calendar expiration = Calendar.getInstance();
		try {
			expiration.set(Calendar.MILLISECOND, 0);
			expiration.setTime(dateFormat.parse(expirationStr));
		} catch (ParseException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid expiration format");
		}
		
		final String userIdentifier = this.getRequest().getAttributes().get("user").toString();
		
		final Session session = new Session(userIdentifier, dataProviderURI, expiration);
		final String idpSessionId = getSessionManager().putSession(session);
		
		final String hostIdentifier = getRequest().getOriginalRef().getHostIdentifier();
		final String validationsURL = hostIdentifier + UserValidationResource.buildUrl(idpSessionId);
		
		return new StringRepresentation(validationsURL);
	}

}
