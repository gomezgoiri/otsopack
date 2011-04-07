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
	
	private static final String SESSIONID_NAME = "sessionid";

	static String buildURL(String sessionId){
		return ROOT + "?" + SESSIONID_NAME + "=" + sessionId;
	}
	
	@Override
	public Representation getValidatedSession() {
		
		return null;
	}

}
