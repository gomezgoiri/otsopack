package otsopack.authn;

import org.restlet.resource.ClientResource;

public interface IClientResourceFactory {
	ClientResource createResource(String url);
}
