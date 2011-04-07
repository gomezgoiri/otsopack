package otsopack.authn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class OtsoAuthnApplication extends Application {
	private final Map<String, Class<?>> resources;
	private final ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	
	private static final String CONTROLLER_PROPERTY_NAME = "controller";
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	public OtsoAuthnApplication(){
		this.resources = PATHS;
	}
	
	@Override
	public Restlet createInboundRoot(){
        Router router = new Router(getContext());
        
	    for(String pattern : this.resources.keySet())
	    	router.attach(pattern, this.resources.get(pattern));
        
        return router;
	}

	public ConcurrentMap<String, Object> getAttributes(){
		return this.attributes;
	}

	public IController getController(){
		return (IController)this.attributes.get(CONTROLLER_PROPERTY_NAME);
	}
	
	public void setController(IController controller){
		this.attributes.put(CONTROLLER_PROPERTY_NAME, controller);
	}

}
