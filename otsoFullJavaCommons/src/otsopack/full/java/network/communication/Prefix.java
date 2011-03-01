package otsopack.full.java.network.communication;

import java.io.Serializable;
import java.net.URI;

public class Prefix implements Serializable {
	String name;
	URI uri;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}	
}
