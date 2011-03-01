package otsopack.full.java.network.communication;

import java.io.Serializable;

public class Prefix implements Serializable {
	private static final long serialVersionUID = -1460421204139612085L;
	
	String name;
	String uri;
	
	public Prefix() {}
	
	public Prefix(String name, String uri) {
		this.name = name;
		this.uri = uri;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return this.uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
