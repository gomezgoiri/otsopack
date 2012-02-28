package otsopack.commons.network.communication.representations;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import otsopack.commons.data.Graph;

public class NTriplesRepresentation extends SemanticFormatRepresentation {

	public NTriplesRepresentation(Graph graph) {
		super(MediaType.TEXT_RDF_NTRIPLES, graph);
	}

	public NTriplesRepresentation(Representation representation) throws IOException {
		super(MediaType.TEXT_RDF_NTRIPLES, representation);
	}

	public NTriplesRepresentation(String data) {
		super(MediaType.TEXT_RDF_NTRIPLES, data);
	}
}
