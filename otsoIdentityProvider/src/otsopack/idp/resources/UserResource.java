package otsopack.idp.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.representation.Representation;

public class UserResource extends AbstractOtsoServerResource implements IUserResource {

	private static String ROOT = "/users/u/{user}";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, UserResource.class);
		return roots;
	}

	
	@Override
	public Representation post(Representation entity) {
		final Form form = new Form(entity);
		final String secret          = form.getFirstValue("secret");
		final String dataProviderURI = form.getFirstValue("dataProviderURI");
		final String expiration      = form.getFirstValue("expiration");
		
		
		
		return null;
	}

}
