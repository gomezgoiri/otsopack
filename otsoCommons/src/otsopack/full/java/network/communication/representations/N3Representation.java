package otsopack.full.java.network.communication.representations;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import otsopack.commons.data.Graph;

public class N3Representation extends SemanticFormatRepresentation {
	
	public N3Representation(Graph graph) {
		super(MediaType.TEXT_RDF_N3, graph);
	}

	public N3Representation(Representation representation)
			throws IOException {
		super(MediaType.TEXT_RDF_N3, representation);
	}

	public N3Representation(String data) {
		super(MediaType.TEXT_RDF_N3, data);
	}
}
