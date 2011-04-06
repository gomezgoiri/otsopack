package otsopack.idp;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Component;
import org.restlet.data.Protocol;

import otsopack.idp.resources.UserResource;
import otsopack.idp.resources.UserValidationResource;

public class RestServer {
	public static final int DEFAULT_PORT = 8182;
	
	private final int port;
	private final Component component;
	private final OtsoIdpApplication application;
	
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		addPaths(UserResource.getRoots());
		addPaths(UserValidationResource.getRoots());
	}
	
	private static void addPaths(Map<String, Class<?>> roots){
		for(String uri : roots.keySet())
			PATHS.put(uri, roots.get(uri));
	}
	
	public RestServer(int port, IController controller) {
		this.port = port;
		
	    this.component = new Component();
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    this.application = new OtsoIdpApplication(RestServer.PATHS);
	    this.application.setController(controller);
	    this.component.getDefaultHost().attach(this.application);
	}
	
	public RestServer(IController controller){
		this(DEFAULT_PORT, controller);
	}
	
	public RestServer(int port){
		this(port, null);
	}
	
	public RestServer(){
		this(DEFAULT_PORT, null);
	}
	
	public OtsoIdpApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
