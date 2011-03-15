package otsopack.full.java.network.communication;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;
import org.restlet.service.MetadataService;

import otsopack.commons.IController;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesStorage;

public class OtsopackApplication extends Application {
	
	private final Map<String, Class<?>> resources;
	private final ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	private final PrefixesStorage prefixesStorage = new PrefixesStorage();
	
	private static final String CONTROLLER_PROPERTY_NAME = "controller";

	
	public OtsopackApplication(Map<String, Class<?>> resources){
		this.resources = resources;
	}
	
	public static void registerExtensions(MetadataService metadataService){
		metadataService.addExtension("turtle", MediaType.APPLICATION_RDF_TURTLE);
		metadataService.addExtension("rdf+xml", MediaType.APPLICATION_RDF_XML);
		// For some reason by default nt is registered in Restlet as TEXT_PLAIN
		metadataService.addExtension("nt", MediaType.TEXT_RDF_NTRIPLES, true); 
		// n3 is already registered
	}
	
	@Override
	public Restlet createInboundRoot(){
		registerExtensions(getMetadataService());
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
	
	public PrefixesStorage getPrefixesStorage(){
		return this.prefixesStorage;
	}
}
