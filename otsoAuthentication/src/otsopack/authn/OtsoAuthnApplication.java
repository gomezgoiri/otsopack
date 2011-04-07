package otsopack.authn;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.ClientResource;

import otsopack.authn.resources.SessionRequestResource;
import otsopack.authn.resources.ValidatedSessionResource;
import otsopack.restlet.commons.AbstractOtsopackApplication;

public class OtsoAuthnApplication extends AbstractOtsopackApplication<IController> {
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	private static final ClientResourceFactory defaultClientResourceFactory = new ClientResourceFactory();
	
	private IClientResourceFactory clientResourceFactory;
	
	static{
		addPaths(SessionRequestResource.getRoots());
		addPaths(ValidatedSessionResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	public OtsoAuthnApplication(){
		super(PATHS);
	}
	
	// For testing purposes
	private IClientResourceFactory getClientResourceFactory(){
		if(this.clientResourceFactory == null)
			return defaultClientResourceFactory;
		
		return this.clientResourceFactory;
	}
	
	public ClientResource createResource(String url){
		return getClientResourceFactory().createResource(url);
	}

	public void setClientResourceFactory(IClientResourceFactory clientResourceFactory) {
		this.clientResourceFactory = clientResourceFactory;
	}
}
