package otsopack.authn.resources;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import otsopack.authn.sessions.Session;

public class SessionRequestResource extends AbstractOtsoServerResource implements ISessionRequestResource {

	public static final String ROOT = "/authn/sessions/";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, SessionRequestResource.class);
		return roots;
	}
	
	@Override
	public Representation postRequest(Representation representation) {
		final Form requestForm = new Form(representation);
		final String redirectURL = requestForm.getFirstValue("redirect");
		final Calendar expiration = Calendar.getInstance();
		expiration.set(Calendar.MILLISECOND, 0);
		expiration.add(Calendar.MINUTE, 5);
		
		final String userIdentifier = requestForm.getFirstValue("userIdentifier");
		final String secret = UUID.randomUUID().toString();
		
		final Session session = new Session(redirectURL, secret, userIdentifier, expiration);
		final String sessionId = getSessionManager().putSession(session);
		
		final String hostIdentifier = getRequest().getOriginalRef().getHostIdentifier();
		final String validationURL = hostIdentifier + ValidatedSessionResource.buildURL(sessionId);
		
		final Form idpForm = new Form();
		
		// return new StringRepresentation(validationURL);
		return null;
	}

}
