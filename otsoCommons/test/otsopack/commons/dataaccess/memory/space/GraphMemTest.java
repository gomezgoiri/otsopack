/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.commons.dataaccess.memory.space;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import otsopack.commons.data.FakeSemanticFactory;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.dataaccess.memory.space.GraphMem;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.commons.sampledata.Example;

public class GraphMemTest extends TestCase {

	private FakeSemanticFactory factory;
	
	protected void setUp() throws Exception {
		super.setUp();
		factory = new FakeSemanticFactory();
		SemanticFactory.initialize(factory);
	}
	
	private void assertNotEquals(int expected, int actual){
		if(expected == actual)
			fail("Expected different to: " + expected + "; actual: " + actual);
	}
	
	public void testHashcode() {
		GraphMem mem1 = new GraphMem("http://graph1");
		GraphMem mem2 = new GraphMem("http://graph1");
		GraphMem mem3 = new GraphMem("http://graph2");
		assertEquals(mem1.hashCode(),mem2.hashCode());
		assertNotEquals(mem1.hashCode(),mem3.hashCode());
		assertNotEquals(mem2.hashCode(),mem3.hashCode());
	}

	public void testWrite() {
		/*Graph graph = Factory.createDefaultGraph();
		graph.add( new Triple(Example.tsubj1, Example.tprop1, Example.tobj3) );
		graph.add( new Triple(Example.tsubj2, Example.tprop2, Example.tobj4) );
		graph.add( new Triple(Example.tsubj3, Example.tprop1, Example.tobj3) );
		graph.add( new Triple(Example.tsubj1, Example.tprop2, Example.tobj4) );
		graph.add( new Triple(Example.tsubj2, Example.tprop1, Example.tobj3) );
		graph.add( new Triple(Example.tsubj3, Example.tprop2, Example.tobj4) );*/
		IGraph graph = (IGraph) EasyMock.createMock(IGraph.class);
		
		GraphMem mem3 = new GraphMem("http://graph/write3/");
		mem3.write(graph);
	}

	public void testContains() throws MalformedTemplateException, TripleParseException {
		final SemanticFactory sf = new SemanticFactory();
		IGraph graph = sf.createEmptyGraph();
		graph.add( factory.createTriple(Example.subj1, Example.prop1, Example.obj3) );
		graph.add( factory.createTriple(Example.subj2, Example.prop2, Example.obj4) );
		graph.add( factory.createTriple(Example.subj3, Example.prop1, "\""+String.valueOf(Example.obj10)+"\"^^<http://www.w3.org/2001/XMLSchema#double>") );
		
		GraphMem mem3 = new GraphMem("http://graph/write3/");
		mem3.write(graph);
		
		assertTrue( mem3.contains(sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> <"+Example.obj3+"> .")) );
		assertTrue( mem3.contains(sf.createTemplate("<"+Example.subj2+"> <"+Example.prop2+"> <"+Example.obj4+"> .")) );
		assertTrue( mem3.contains(sf.createTemplate("<"+Example.subj3+"> <"+Example.prop1+"> \""+String.valueOf(Example.obj10)+"\"^^<http://www.w3.org/2001/XMLSchema#double> .")) );
		assertTrue( mem3.contains(sf.createTemplate("?s ?p ?o .")) );
		assertTrue( mem3.contains(sf.createTemplate("<"+Example.subj2+"> ?p <"+Example.obj4+"> .")) );
		assertFalse( mem3.contains(sf.createTemplate("<"+Example.subj4+"> ?p ?o .")) );
	}
}