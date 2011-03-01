package otsopack.full.java.network.communication;

import java.io.Serializable;
import java.net.URI;

public class Prefix implements Serializable {
	private static final long serialVersionUID = -1460421204139612085L;
	
	String name;
	URI uri;
	
	public Prefix() {}
	
	public Prefix(String name, URI uri) {
		this.name = name;
		this.uri = uri;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public URI getUri() {
		return this.uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}	
}
