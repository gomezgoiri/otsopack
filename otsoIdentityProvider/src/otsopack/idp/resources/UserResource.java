package otsopack.idp.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.representation.Representation;

public class UserResource extends AbstractOtsoServerResource implements IUserResource {

	private static String ROOT = "/users/u/{user}";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, UserResource.class);
		return roots;
	}

	
	@Override
	public Representation post(Representation representation) {
		// TODO Auto-generated method stub
		return null;
	}

}
