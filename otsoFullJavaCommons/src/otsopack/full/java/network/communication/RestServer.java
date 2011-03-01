package otsopack.full.java.network.communication;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class RestServer {
	public static final int DEFAULT_PORT = 8182;
	private final int port;
	private final Component component;
	
	private static final Map<String, Class<?>> PATHS = new HashMap<String, Class<?>>();
	
	static{
		PATHS.put("/user/{user}", PrefixManager.class);
		PATHS.put("/prefixes",    PrefixManager.class);
		
		PATHS.put("/graphs",           GraphsManager.class);
		PATHS.put("/graphs/wildcard",  WildcardGraphManager.class);
		
	}
	
	public RestServer(int port) {
		this.port = port;
		
	    this.component = new Component();  
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    for(String pattern : RestServer.PATHS.keySet())
	    	this.component.getDefaultHost().attach(pattern, RestServer.PATHS.get(pattern));
	}
	
	public RestServer(){
		this(DEFAULT_PORT);
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void attach(String pattern, Class<?> targetClass){
		this.component.getDefaultHost().attach(pattern, targetClass);
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
