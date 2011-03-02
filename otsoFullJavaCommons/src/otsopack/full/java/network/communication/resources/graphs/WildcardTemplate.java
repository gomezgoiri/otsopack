package otsopack.full.java.network.communication.resources.graphs;

import java.io.Serializable;

public class WildcardTemplate implements Serializable {
	private static final long serialVersionUID = -4089695320142349360L;
	
	private final String subject;
	private final String predicate;
	private final String object;
	
	public WildcardTemplate(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getPredicate() {
		return this.predicate;
	}

	public String getObject() {
		return this.object;
	}
}
