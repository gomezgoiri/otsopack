package otsopack.authn;

import org.restlet.resource.ClientResource;

public class ClientResourceFactory implements IClientResourceFactory {
	
	public ClientResource createResource(String url){
		return new ClientResource(url);
	}
	
}
