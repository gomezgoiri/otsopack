package otsopack.full.java.network.communication.resources.graphs;

import org.junit.Test;
import static org.junit.Assert.*;
import org.restlet.resource.ClientResource;

import otsopack.full.java.network.communication.AbstractRestServerTesting;
import otsopack.full.java.network.communication.resources.graphs.IGraphsResource;
import otsopack.full.java.network.communication.util.JSONDecoder;

public class GraphsTest extends AbstractRestServerTesting {
	@Test
	public void testQueryGraph() throws Exception {
		final ClientResource cr = new ClientResource(getBaseURL() + "graphs");
		final IGraphsResource prefrsc = cr.wrap(IGraphsResource.class);
		
		final String prefixes = prefrsc.toJson();
		
		final String [] results = JSONDecoder.decode(prefixes, String[].class);
		
		assertEquals(3, results.length);
		assertEquals("/graphs", results[0]);
		assertEquals("/graphs/wildcards", results[1]);
		assertEquals("/graphs/wildcards/{subject}/{predicate}/{object}", results[2]);
	}

}
