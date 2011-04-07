package otsopack.authn;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class RestServer {
	public static final int DEFAULT_PORT = 8183;
	
	private final int port;
	private final Component component;
	private final OtsoAuthnApplication application;
	
	public RestServer(int port, IController controller) {
		this.port = port;
		
	    this.component = new Component();
	    this.component.getServers().add(Protocol.HTTP, this.port);
	    
	    this.application = new OtsoAuthnApplication();
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
	
	public OtsoAuthnApplication getApplication(){
		return this.application;
	}
	
	public void startup() throws Exception {
		this.component.start();
	}
	
	public void shutdown() throws Exception {
		this.component.stop();
	}
}
