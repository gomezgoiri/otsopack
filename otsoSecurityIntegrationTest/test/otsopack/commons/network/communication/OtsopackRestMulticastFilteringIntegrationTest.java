package otsopack.commons.network.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import otsopack.commons.Arguments;
import otsopack.commons.OtsoServerManager;
import otsopack.commons.authz.Filter;
import otsopack.commons.authz.asserts.ContainsURIAssert;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.WildcardTemplate;

public class OtsopackRestMulticastFilteringIntegrationTest extends
		AbstractOtsopackRestMulticastIntegrationTest {

	private final Filter filterA = new Filter(this.AITOR, new ContainsURIAssert(OtsoServerManager.AITOR_DEPICTION));
	private final Filter filterB = new Filter(this.PABLO, new ContainsURIAssert(OtsoServerManager.PABLO_DEPICTION));
	// Only Pablo can talk about yoda
	private final Filter filterC = new Filter(this.PABLO, new ContainsURIAssert(OtsoServerManager.YODA_DEPICTION)); 
	private final Filter [] filters = new Filter[]{ this.filterA, this.filterB, this.filterC };
	
	private final Arguments arguments = new Arguments().setOutputFormat(SemanticFormat.NTRIPLES).setFilters(this.filters);
	
	// READ
	
	@Test
	public void testReadUriFiltering() throws Exception {
		final Graph graphA = this.ruc.read(OtsoServerManager.SPACE, this.nodeA.getGraphUris().get(0), this.arguments);
		assertGraphEquals(OtsoServerManager.AITOR_GRAPH, graphA);
		
		final Graph graphB = this.ruc.read(OtsoServerManager.SPACE, this.nodeB.getGraphUris().get(0), this.arguments);
		assertGraphEquals(OtsoServerManager.PABLO_GRAPH, graphB);
		
		// Node C does not sign messages, and we don't trust yoda messages not sent by pablo
		final Graph graphC = this.ruc.read(OtsoServerManager.SPACE, this.nodeC.getGraphUris().get(0), this.arguments);
		assertNull(graphC);
	}

	
	@Test
	public void testReadTemplate() throws Exception {
		final Graph graphA = this.ruc.read(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, null, OtsoServerManager.AITOR_DEPICTION), this.arguments);
		assertGraphEquals(OtsoServerManager.AITOR_GRAPH, graphA);
		
		final Graph graphB = this.ruc.read(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, null, OtsoServerManager.PABLO_DEPICTION), this.arguments);
		assertGraphEquals(OtsoServerManager.PABLO_GRAPH, graphB);
		
		// Node C does not sign messages, and we don't trust yoda messages not sent by pablo
		final Graph graphC = this.ruc.read(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, null, OtsoServerManager.YODA_DEPICTION), this.arguments);
		assertNull(graphC);
	}
	
	// TAKE
	
	@Test
	public void testTakeURI() throws Exception {
		Graph graphA = this.ruc.take(OtsoServerManager.SPACE, this.nodeA.getGraphUris().get(0), this.arguments);
		assertGraphEquals(OtsoServerManager.AITOR_GRAPH, graphA);
		// Assert deleted
		graphA = this.ruc.take(OtsoServerManager.SPACE, this.nodeA.getGraphUris().get(0), this.arguments);
		assertNull(graphA);
		
		Graph graphB = this.ruc.take(OtsoServerManager.SPACE, this.nodeB.getGraphUris().get(0), this.arguments);
		assertGraphEquals(OtsoServerManager.PABLO_GRAPH, graphB);
		// Assert deleted
		graphB = this.ruc.take(OtsoServerManager.SPACE, this.nodeB.getGraphUris().get(0), this.arguments);
		assertNull(graphB);
		
		// Node C does not sign messages, and we don't trust yoda messages not sent by pablo
		Graph graphC = this.ruc.take(OtsoServerManager.SPACE, this.nodeC.getGraphUris().get(0), this.arguments);
		assertNull(graphC);
	}
	
	@Test
	public void testTakeTemplate() throws Exception {
		Graph graphA = this.ruc.take(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, null, OtsoServerManager.AITOR_DEPICTION), this.arguments);
		assertGraphEquals(OtsoServerManager.AITOR_GRAPH, graphA);
		// Assert deleted
		graphA = this.ruc.take(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, null, OtsoServerManager.AITOR_DEPICTION), this.arguments);
		assertNull(graphA);
		
		Graph graphB = this.ruc.take(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, null, OtsoServerManager.PABLO_DEPICTION), this.arguments);
		assertGraphEquals(OtsoServerManager.PABLO_GRAPH, graphB);
		// Assert deleted
		graphB = this.ruc.take(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, null, OtsoServerManager.PABLO_DEPICTION), this.arguments);
		assertNull(graphB);
		
		// Node C does not sign messages, and we don't trust yoda messages not sent by pablo
		Graph graphC = this.ruc.take(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, null, OtsoServerManager.YODA_DEPICTION), this.arguments);
		assertNull(graphC);
	}
	
	
	@Test
	public void testQueryAll() throws Exception {
		final Graph [] returnedGraphs = this.ruc.query(OtsoServerManager.SPACE, WildcardTemplate.createWithNull(null, OtsoServerManager.DEPICTION), this.arguments);
		// Only two nodes are taken into account, since the yoda one is not signed
		assertNotNull(returnedGraphs);
		assertEquals(2, returnedGraphs.length);
		
		for(Graph graph : returnedGraphs)
			assertTrue(
					"No known depiction found in returned graph: " + graph,
					graph.getData().indexOf(OtsoServerManager.AITOR_DEPICTION) >= 0
					|| graph.getData().indexOf(OtsoServerManager.PABLO_DEPICTION) >= 0
				);
	}
	
	@Test
	public void testQueryOne() throws Exception {
		final Graph [] returnedGraphs = this.ruc.query(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, OtsoServerManager.DEPICTION, OtsoServerManager.PABLO_DEPICTION), this.arguments);
		// A single node returned something
		assertNotNull(returnedGraphs);
		assertEquals(1, returnedGraphs.length);
		assertTrue(returnedGraphs[0].getData().indexOf(OtsoServerManager.PABLO_DEPICTION) >= 0);
	}
	
	@Test
	public void testQueryNone() throws Exception {
		final Graph [] returnedGraphs = this.ruc.query(OtsoServerManager.SPACE, WildcardTemplate.createWithURI(null, OtsoServerManager.DEPICTION, "http://this.does.not.exist"), this.arguments);
		assertNull(returnedGraphs);
	}
}
