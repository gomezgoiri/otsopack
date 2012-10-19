/*
 * Copyright (C) 2008 onwards University of Deusto
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.sampledata.Example;

public class GraphMemTest {
	
	@Before
	public void setUp() throws Exception {
		SemanticFactory.initialize(new MicrojenaFactory());
	}
	
	private void assertNotEquals(int expected, int actual){
		if(expected == actual)
			fail("Expected different to: " + expected + "; actual: " + actual);
	}
	
	@Test
	public void testHashcode() {
		GraphMem mem1 = new GraphMem("http://graph1");
		GraphMem mem2 = new GraphMem("http://graph1");
		GraphMem mem3 = new GraphMem("http://graph2");
		assertEquals(mem1.hashCode(),mem2.hashCode());
		assertNotEquals(mem1.hashCode(),mem3.hashCode());
		assertNotEquals(mem2.hashCode(),mem3.hashCode());
	}

	@Test
	public void testWrite() {
		/*Graph graph = Factory.createDefaultGraph();
		graph.add( new Triple(Example.tsubj1, Example.tprop1, Example.tobj3) );
		graph.add( new Triple(Example.tsubj2, Example.tprop2, Example.tobj4) );
		graph.add( new Triple(Example.tsubj3, Example.tprop1, Example.tobj3) );
		graph.add( new Triple(Example.tsubj1, Example.tprop2, Example.tobj4) );
		graph.add( new Triple(Example.tsubj2, Example.tprop1, Example.tobj3) );
		graph.add( new Triple(Example.tsubj3, Example.tprop2, Example.tobj4) );*/
		final ModelImpl graph = new ModelImpl();
		
		final GraphMem mem3 = new GraphMem("http://graph/write3/");
		mem3.write(graph);
	}

	@Test
	public void testContains() throws Exception {
		final SemanticFactory sf = new SemanticFactory();
		final String triples =	"<"+Example.subj1+"> <"+Example.prop1+"> <"+Example.obj3+"> .\n" +
								"<"+Example.subj2+"> <"+Example.prop2+"> <"+Example.obj4+"> .\n" +
								"<"+Example.subj3+"> <"+Example.prop1+"> \""+String.valueOf(Example.obj10)+"\"^^<http://www.w3.org/2001/XMLSchema#double> .\n";
		final ModelImpl graph = new ModelImpl();
		graph.read( new Graph(triples, SemanticFormat.NTRIPLES) );
		
		final GraphMem mem3 = new GraphMem("http://graph/write3/");
		mem3.write(graph);
		
		assertTrue( mem3.contains(sf.createTemplate("<"+Example.subj1+"> <"+Example.prop1+"> <"+Example.obj3+"> .")) );
		assertTrue( mem3.contains(sf.createTemplate("<"+Example.subj2+"> <"+Example.prop2+"> <"+Example.obj4+"> .")) );
		assertTrue( mem3.contains(sf.createTemplate("<"+Example.subj3+"> <"+Example.prop1+"> \""+String.valueOf(Example.obj10)+"\"^^<http://www.w3.org/2001/XMLSchema#double> .")) );
		assertTrue( mem3.contains(sf.createTemplate("?s ?p ?o .")) );
		assertTrue( mem3.contains(sf.createTemplate("<"+Example.subj2+"> ?p <"+Example.obj4+"> .")) );
		assertFalse( mem3.contains(sf.createTemplate("<"+Example.subj4+"> ?p ?o .")) );
	}
}