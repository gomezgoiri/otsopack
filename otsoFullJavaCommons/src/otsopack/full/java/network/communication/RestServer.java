package otsopack.full.java.network.communication;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class RestServer {
	Component component;
	
	public RestServer() {
		
	}
	
	public void startup() throws Exception {        
	    component = new Component();  
	    component.getServers().add(Protocol.HTTP, 8182);
	    
	    // Now, let's start the component!  
	    // Note that the HTTP server connector is also automatically started.  
	    component.start();
	    
	    component.getDefaultHost().attach("/user/{user}", PrefixManager.class);
	    component.getDefaultHost().attach("/prefixes", PrefixManager.class);  
	}
	
	public void shutdown() throws Exception {
		component.stop();
	}
}
