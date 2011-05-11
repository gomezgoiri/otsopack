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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 */
package otsopack.full.java;

import java.util.List;
import java.util.Vector;

import org.easymock.EasyMock;

import otsopack.commons.IController;
import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.authz.entities.User;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.OtsoRestServer;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesStorage;

public class OtsoServerManager {
	
	private final int otsoTestingPort;
	protected OtsoRestServer rs;
	protected IController controller;
	private List<String> writtenGraphURIs = new Vector<String>();
	public User user1 = new User("pablo");
	public User user2 = new User("aitor");
	
	public static final String DEPICTION = "http://xmlns.com/foaf/0.1/depiction";
	
	public static final String SPACE = "http://testSpace.com/space/";
	
	public static final String AITOR_DEPICTION = "http://aitor.gomezgoiri.net/profile.jpg";
	
	public static final Graph AITOR_GRAPH = new Graph(
			"<http://aitor.gomezgoiri.net/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/name> \"Aitor Gomez-Goiri\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/title> \"Sr\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/givenname> \"Aitor\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/family_name> \"Gomez-Goiri\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net> . \n" +
			"<http://aitor.gomezgoiri.net/me> <" + DEPICTION + "> <" + AITOR_DEPICTION + "> . \n",
			SemanticFormat.NTRIPLES);
	
	public static final String PABLO_DEPICTION = "http://paginaspersonales.deusto.es/porduna/images/porduna.png";
	
	public static final Graph PABLO_GRAPH = new Graph(
			"<http://pablo.ordunya.com/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/name> \"Pablo Orduna\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/title> \"Sr\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/givenname> \"Pablo\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/family_name> \"Orduna\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/homepage> <http://pablo.ordunya.com> . \n" +
			"<http://pablo.ordunya.com/me> <" + DEPICTION + "> <" + PABLO_DEPICTION + "> . \n",
			SemanticFormat.NTRIPLES);
	
	public static final String YODA_DEPICTION = "http://upload.wikimedia.org/wikipedia/en/9/96/CGIYoda.jpg";
	
	public static final Graph YODA_GRAPH = new Graph(
			"<http://facebook.com/user/yoda> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/name> \"Yoda\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/title> \"Jedi\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/givenname> \"Yoda\"^^<http://www.w3.org/2001/XMLSchema#string> . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com> . \n" +
			"<http://facebook.com/user/yoda> <" + DEPICTION + "> <" + YODA_DEPICTION + "> . \n",
			SemanticFormat.NTRIPLES);
	
	
	public OtsoServerManager(int otsoTestingPort, IEntity signer){
		this(otsoTestingPort, signer, null, true);
	}
	
	public OtsoServerManager(int otsoTestingPort, IEntity signer, ICommunication multicastProvider, boolean provideController){
		this.otsoTestingPort = otsoTestingPort;
		if(provideController){
			this.controller = EasyMock.createMock(IController.class);
			//EasyMock.expect(this.controller.getDataAccessService()).andReturn(new FakeDataAccess()).anyTimes();
			EasyMock.expect(this.controller.getDataAccessService()).andReturn(new MemoryDataAccess()).anyTimes();
			EasyMock.replay(this.controller);
		}else
			this.controller = null;
		
		this.rs = new OtsoRestServer(otsoTestingPort, this.controller, signer, multicastProvider);
	}
	
	public void start() throws Exception {
		this.rs.startup();
	}
	
	public List<String> getGraphUris(){
		return this.writtenGraphURIs;
	}
	
	public void prepareSemanticRepository() throws TSException {
		this.controller.getDataAccessService().startup();
		this.controller.getDataAccessService().createSpace(SPACE);
		this.controller.getDataAccessService().joinSpace(SPACE);
	}
	
	public void addGraph(Graph graph, User user) throws TSException {
		this.writtenGraphURIs.add(this.controller.getDataAccessService().write(SPACE, graph, user));
	}
	
	public void addGraph(Graph graph) throws TSException {
		this.writtenGraphURIs.add(this.controller.getDataAccessService().write(SPACE, graph));
	}
	
	public void stop() throws Exception {
		this.rs.shutdown();
	}
	
	protected PrefixesStorage getPrefixesStorage(){
		return this.rs.getApplication().getPrefixesStorage();
	}
	
	protected String getOtsoServerBaseURL(){
		return "http://127.0.0.1:" + this.otsoTestingPort;
	}
	

}
