package otsopack.authn.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.representation.Representation;

public class ValidatedSessionResource extends AbstractOtsoServerResource implements IValidatedSessionResource {

	private static final String ROOT = "/authn/sessions/valid";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, ValidatedSessionResource.class);
		return roots;
	}
	
	public static final String SESSIONID_NAME = "sessionid";
	public static final String SECRET_NAME = "secret";

	static String buildURL(String sessionId, String secret){
		return ROOT + "?" + SESSIONID_NAME + "=" + sessionId + "&" + SECRET_NAME + "=" + secret;
	}
	
	@Override
	public Representation getValidatedSession() {
		
		return null;
	}

}
