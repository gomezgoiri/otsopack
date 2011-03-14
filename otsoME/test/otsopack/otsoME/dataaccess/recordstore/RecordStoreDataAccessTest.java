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
package otsopack.otsoME.dataaccess.recordstore;

import jmunit.framework.cldc11.TestCase;
import otsopack.commons.data.Graph;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ISemanticFactory;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.ITriple;
import otsopack.commons.data.SemanticFormats;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.otsoME.dataaccess.recordstore.space.SpaceRecord;
import otsopack.otsoME.sampledata.ExampleME;

public class RecordStoreDataAccessTest extends TestCase {
	
	private MicrojenaFactory factory;
	
	public RecordStoreDataAccessTest() {
		super(12, "RecordStoreTest");
	}	
	
	public void setUp()	throws Throwable {
		super.setUp();
		factory = new MicrojenaFactory();
		SemanticFactory.initialize(factory);
	}
	
	public void tearDown() {
	}
	
	public void test(int testNumber) throws Throwable {
		switch ( testNumber ) {
			case 0: testCreateSpace();
				break;
			case 1: testJoinSpace();
				break;
			case 2: testJoinSpaceFailure();
				break;
			case 3: testLeaveSpace();
				break;
			case 4: testLeaveSpaceFailure();
				break;
			case 5: testWriteList();
				break;	
			case 6: testQuery();
				break;
			case 7: testEmptyQuery();
				break;
			case 8: testReadTemplate();
				break;
			case 9: testReadURI();
				break;
			case 10: testTakeTemplate();
				break;
			case 11: testTakeURI();
				break;
			default:
				break;	
		}
	}
	
	public void testCreateSpace() throws Exception {
		RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		
		try {
			memo.createSpace("ts://espacio/");
			memo.createSpace("ts://espacio2/");
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		
		for(int i=0; i<memo.spaces.size(); i++) {
			String storeName1 = ((SpaceRecord) memo.spaces.elementAt(i)).getRecordStoreName();
			for(int j=(i+1); j<memo.spaces.size(); j++) {
				String storeName2 = ((SpaceRecord) memo.spaces.elementAt(j)).getRecordStoreName();
				if(storeName1.equals(storeName2)) throw new Exception("The name generator has not worked");
			}
		}
		
		memo.shutdown();
	}

	public void testJoinSpace() throws TSException {
		RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace("ts://espacio2");
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace("ts://espacio2");
		memo.shutdown();
	}
	
	public void testJoinSpaceFailure() throws TSException {
		RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace("ts://espacio3");
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		
		try {
			memo.joinSpace("ts://space_that_doesnt_exist");
			assertTrue(false);
		} catch (SpaceNotExistsException e) {
			assertTrue(true);
		}
		memo.shutdown();
	}

	public void testLeaveSpace() throws TSException {
		RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace("ts://espacio4");
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace("ts://espacio4");
		memo.leaveSpace("ts://espacio4");
		memo.shutdown();
	}
	
	public void testLeaveSpaceFailure() throws TSException {
		RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace("ts://espacio5");
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace("ts://espacio5");
		try {
			memo.leaveSpace("ts://espacio6");
			assertTrue(false);
		} catch (Exception e) {
			//assertTrue(true);
		}
		memo.shutdown();
	}

	public void testWriteList() throws TSException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceURI = "ts://espacioWrite2";
		final ITriple[] trips = new ITriple[4];
		final RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace(spaceURI);
        
		final IGraph triples = sf.createEmptyGraph();
		triples.add( trips[0] = factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[1] = factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[2] = factory.createTriple(ExampleME.subj3, ExampleME.prop3, ExampleME.obj3) );
		triples.add( trips[3] = factory.createTriple(ExampleME.subj4, ExampleME.prop4, ExampleME.obj4) );
		
		final String graphuri = memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		memo.leaveSpace(spaceURI);
		memo.shutdown();

		// To test persistence, we check with a different object
		final RecordStoreDataAccess memo2 = new RecordStoreDataAccess();
		memo2.startup();
		try {
			memo2.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			e.printStackTrace();
		}
		memo2.joinSpace(spaceURI);
		SpaceRecord m = memo2.getSpace(spaceURI);
				
		assertNotNull(graphuri);
		assertTrue( m.contains(trips[1]) );
		assertTrue( m.contains(trips[2]) );
		assertTrue( m.contains(trips[3]) );
		assertTrue( m.containsGraph(graphuri) );
		
		memo2.leaveSpace(spaceURI);
		memo2.shutdown();
	}
	
	public void testQuery() throws TSException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceURI = "ts://espacioQuery/";
		final ITriple[] trips = new ITriple[3];
		final RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace(spaceURI);
		
		final ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> <"+ExampleME.prop1+"> ?o .");
		final IGraph triples = sf.createEmptyGraph();
		triples.add( trips[0] = factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[1] = factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[2] = factory.createTriple(ExampleME.subj3, ExampleME.prop3, ExampleME.obj3) );
		
		memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		memo.leaveSpace(spaceURI);
		memo.shutdown();
		
		// To test persistence, we check with a different object
		final RecordStoreDataAccess memo2 = new RecordStoreDataAccess();
		memo2.startup();
		try {
			memo2.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			e.printStackTrace();
		}
		memo2.joinSpace(spaceURI);
		final Graph ret = memo2.query(spaceURI, sel, SemanticFormats.NTRIPLES);
		memo2.leaveSpace(spaceURI);
		memo2.shutdown();
		
		assertNotNull( ret );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[0]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[1]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[2]) );
	}
	
	public void testEmptyQuery() throws TSException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceURI = "ts://espacioQuery2/";
		final ITriple[] trips = new ITriple[3];
		final RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace(spaceURI);
		
		final ITemplate sel = sf.createTemplate("<"+ExampleME.subj2+"> <"+ExampleME.prop1+"> ?o .");
		IGraph triples = sf.createEmptyGraph();
		triples.add( trips[0] = factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[1] = factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[2] = factory.createTriple(ExampleME.subj3, ExampleME.prop3, ExampleME.obj3) );

		memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		final Graph ret = memo.query(spaceURI, sel, SemanticFormats.NTRIPLES);
		memo.leaveSpace(spaceURI);
		memo.shutdown();
		
		assertNull( ret );
	}
	
	public void testReadTemplate() throws TSException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceURI = "ts://espacioRead1/";
		final ITriple[] trips = new ITriple[6];
		final RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace(spaceURI);
        
		IGraph triples = sf.createEmptyGraph();
		triples.add( trips[0] = factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[1] = factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[2] = factory.createTriple(ExampleME.subj3, ExampleME.prop3, ExampleME.obj3) );
		memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		triples = sf.createEmptyGraph();
		triples.add( trips[3] = factory.createTriple(ExampleME.subj4, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[4] = factory.createTriple(ExampleME.subj5, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[5] = factory.createTriple(ExampleME.subj6, ExampleME.prop3, ExampleME.obj3) );
		memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		final ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final ITemplate sel2 = sf.createTemplate("<"+ExampleME.subj5+"> <"+ExampleME.prop2+"> ?o .");
		final Graph ret = memo.read(spaceURI, sel, SemanticFormats.NTRIPLES);
		final Graph ret2 = memo.read(spaceURI, sel2, SemanticFormats.NTRIPLES);
		final Graph ret3 = memo.read(spaceURI, sel, SemanticFormats.NTRIPLES);
	
		// We check if the first read has returned the correct triples
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[0]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[1]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[2]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[3]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[4]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[5]) );
			
		// We check if the second read has returned the correct triples
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[0]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[1]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[2]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[3]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[4]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[5]) );
		
		// If we do the same query again, same result
		assertTrue( new ModelImpl(ret3).getIGraph().contains(trips[0]) );
		assertTrue( new ModelImpl(ret3).getIGraph().contains(trips[1]) );
		assertTrue( new ModelImpl(ret3).getIGraph().contains(trips[2]) );
		assertFalse( new ModelImpl(ret3).getIGraph().contains(trips[3]) );
		assertFalse( new ModelImpl(ret3).getIGraph().contains(trips[4]) );
		assertFalse( new ModelImpl(ret3).getIGraph().contains(trips[5]) );
		
		// We check if triples remain in the space
		SpaceRecord m = memo.getSpace(spaceURI);
		assertTrue( m.contains(trips[0]) );
		assertTrue( m.contains(trips[1]) );
		assertTrue( m.contains(trips[2]) );
		assertTrue( m.contains(trips[3]) );
		assertTrue( m.contains(trips[4]) );
		assertTrue( m.contains(trips[5]) );
		
		memo.leaveSpace(spaceURI);
		memo.shutdown();
	}
	
	public void testReadURI() throws TSException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceURI = "ts://espacioRead2/";
		final ITriple[] trips = new ITriple[6];
		final String[] graphsuri = new String[2];
		final RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace(spaceURI);
        
		IGraph triples = sf.createEmptyGraph();
		triples.add( trips[0] = factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[1] = factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[2] = factory.createTriple(ExampleME.subj3, ExampleME.prop3, ExampleME.obj3) );
		graphsuri[0] = memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		triples = sf.createEmptyGraph();
		triples.add( trips[3] = factory.createTriple(ExampleME.subj4, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[4] = factory.createTriple(ExampleME.subj5, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[5] = factory.createTriple(ExampleME.subj6, ExampleME.prop3, ExampleME.obj3) );
		graphsuri[1] = memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		final Graph ret = memo.read(spaceURI, graphsuri[1], SemanticFormats.NTRIPLES);
		final Graph ret2 = memo.read(spaceURI, graphsuri[0], SemanticFormats.NTRIPLES);
		final Graph ret3 = memo.read(spaceURI, graphsuri[1], SemanticFormats.NTRIPLES);
	
		// We check if the first read has returned the correct triples
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[0]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[1]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[2]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[3]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[4]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[5]) );
			
		// We check if the second read has returned the correct triples
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[0]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[1]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[2]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[3]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[4]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[5]) );
		
		// If we do the same query again, same result
		assertFalse( new ModelImpl(ret3).getIGraph().contains(trips[0]) );
		assertFalse( new ModelImpl(ret3).getIGraph().contains(trips[1]) );
		assertFalse( new ModelImpl(ret3).getIGraph().contains(trips[2]) );
		assertTrue( new ModelImpl(ret3).getIGraph().contains(trips[3]) );
		assertTrue( new ModelImpl(ret3).getIGraph().contains(trips[4]) );
		assertTrue( new ModelImpl(ret3).getIGraph().contains(trips[5]) );
		
		// We check if triples remain in the space
		SpaceRecord m = memo.getSpace(spaceURI);
		assertTrue( m.contains(trips[0]) );
		assertTrue( m.contains(trips[1]) );
		assertTrue( m.contains(trips[2]) );
		assertTrue( m.contains(trips[3]) );
		assertTrue( m.contains(trips[4]) );
		assertTrue( m.contains(trips[5]) );
		
		memo.leaveSpace(spaceURI);
		memo.shutdown();
	}
	
	public void testTakeTemplate() throws TSException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceURI = "ts://espacioTake/";
		final ITriple[] trips = new ITriple[6];
		final RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution.
		}
		memo.joinSpace(spaceURI);
        
		IGraph triples = sf.createEmptyGraph();
		triples.add( trips[0] = factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[1] = factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[2] = factory.createTriple(ExampleME.subj3, ExampleME.prop3, ExampleME.obj3) );
		memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		triples = sf.createEmptyGraph();
		triples.add( trips[3] = factory.createTriple(ExampleME.subj4, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[4] = factory.createTriple(ExampleME.subj5, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[5] = factory.createTriple(ExampleME.subj6, ExampleME.prop3, ExampleME.obj3) );
		memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		final ITemplate sel = sf.createTemplate("<"+ExampleME.subj1+"> ?p ?o .");
		final ITemplate sel2 = sf.createTemplate("<"+ExampleME.subj5+"> <"+ExampleME.prop2+"> ?o .");
		final Graph ret = memo.take(spaceURI, sel, SemanticFormats.NTRIPLES);
		final Graph ret2 = memo.take(spaceURI, sel2, SemanticFormats.NTRIPLES);
		final Graph ret3 = memo.take(spaceURI, sel, SemanticFormats.NTRIPLES);
	
		// We check if the first read has returned the correct triples
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[0]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[1]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[2]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[3]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[4]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[5]) );
			
		// We check if the second read has returned the correct triples
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[0]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[1]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[2]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[3]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[4]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[5]) );
		
		// If we do the same query again, the triples shouldn't be in the space because of the "take"
		assertNull( ret3 );
		
		// We check if triples remain in the space
		SpaceRecord m = memo.getSpace(spaceURI);
		assertFalse( m.contains(trips[0]) );
		assertFalse( m.contains(trips[1]) );
		assertFalse( m.contains(trips[2]) );
		assertFalse( m.contains(trips[3]) );
		assertFalse( m.contains(trips[4]) );
		assertFalse( m.contains(trips[5]) );
		
		memo.leaveSpace(spaceURI);
		memo.shutdown();
	}
	
	public void testTakeURI() throws TSException {
		final ISemanticFactory sf = new SemanticFactory();
		final String spaceURI = "ts://espacioRead2/";
		final ITriple[] trips = new ITriple[6];
		final String[] graphsuri = new String[2];
		RecordStoreDataAccess memo = new RecordStoreDataAccess();
		memo.startup();
		try {
			memo.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution
		}
		memo.joinSpace(spaceURI);
        
		IGraph triples = sf.createEmptyGraph();
		triples.add( trips[0] = factory.createTriple(ExampleME.subj1, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[1] = factory.createTriple(ExampleME.subj2, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[2] = factory.createTriple(ExampleME.subj3, ExampleME.prop3, ExampleME.obj3) );
		graphsuri[0] = memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		triples = sf.createEmptyGraph();
		triples.add( trips[3] = factory.createTriple(ExampleME.subj4, ExampleME.prop1, ExampleME.obj1) );
		triples.add( trips[4] = factory.createTriple(ExampleME.subj5, ExampleME.prop2, ExampleME.obj2) );
		triples.add( trips[5] = factory.createTriple(ExampleME.subj6, ExampleME.prop3, ExampleME.obj3) );
		graphsuri[1] = memo.write(spaceURI, new ModelImpl(triples).write(SemanticFormats.NTRIPLES));
		
		memo.leaveSpace(spaceURI);
		memo.shutdown();
		
		//different StoreManager object, to check if storage works
		RecordStoreDataAccess memo2 = new RecordStoreDataAccess();
		memo2.startup();
		try {
			memo2.createSpace(spaceURI);
		} catch(SpaceAlreadyExistsException e) {
			// They have been created in a previous execution
		}
		memo2.joinSpace(spaceURI);
		
		final Graph ret = memo2.take(spaceURI, graphsuri[1], SemanticFormats.NTRIPLES);
		final Graph ret2 = memo2.take(spaceURI, graphsuri[0], SemanticFormats.NTRIPLES);
		final Graph ret3 = memo2.take(spaceURI, graphsuri[1], SemanticFormats.NTRIPLES);
	
		// We check if the first read has returned the correct triples
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[0]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[1]) );
		assertFalse( new ModelImpl(ret).getIGraph().contains(trips[2]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[3]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[4]) );
		assertTrue( new ModelImpl(ret).getIGraph().contains(trips[5]) );
			
		// We check if the second read has returned the correct triples
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[0]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[1]) );
		assertTrue( new ModelImpl(ret2).getIGraph().contains(trips[2]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[3]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[4]) );
		assertFalse( new ModelImpl(ret2).getIGraph().contains(trips[5]) );
		
		// If we do the same query again, the triples shouldn't be in the space because of the "take"
		assertNull( ret3 );
		
		// We check if triples remain in the space
		SpaceRecord m = memo.getSpace(spaceURI);
		assertFalse( m.contains(trips[0]) );
		assertFalse( m.contains(trips[1]) );
		assertFalse( m.contains(trips[2]) );
		assertFalse( m.contains(trips[3]) );
		assertFalse( m.contains(trips[4]) );
		assertFalse( m.contains(trips[5]) );
		
		memo2.leaveSpace(spaceURI);
		memo2.shutdown();
	}
}
