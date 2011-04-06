package otsopack.idp.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

public class UserValidationResource extends AbstractOtsoServerResource implements IUserValidationResource {

	static String ROOT = "/users/validations";
	
	public static Map<String, Class<?>> getRoots() {
		final Map<String, Class<?>> roots = new HashMap<String, Class<?>>();
		roots.put(ROOT, UserValidationResource.class);
		return roots;
	}
	
	@Override
	public Representation post(Representation entity) {
		final Form form = new Form(entity);
		
		final String username = form.getFirstValue("username");
		final String password = form.getFirstValue("password");
		
		final boolean authenticated = getCredentialsChecker().checkCredentials(username, password);
		
		// TODO
		return new StringRepresentation(Boolean.toString(authenticated));
	}
}
