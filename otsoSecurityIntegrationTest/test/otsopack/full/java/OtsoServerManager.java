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
	
	public static final String SPACE = "http://testSpace.com/space/";
	
	public static final Graph AITOR_GRAPH = new Graph(
			"<http://aitor.gomezgoiri.net/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/name> \"Aitor Gómez-Goiri\" . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/title> \"Sr\" . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/givenname> \"Aitor\" . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/family_name> \"Gómez-Goiri\" . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/homepage> <http://aitor.gomezgoiri.net> . \n" +
			"<http://aitor.gomezgoiri.net/me> <http://xmlns.com/foaf/0.1/depiction> <http://aitor.gomezgoiri.net/profile.jpg> . \n",
			SemanticFormat.NTRIPLES);
	
	public static final Graph PABLO_GRAPH = new Graph(
			"<http://pablo.ordunya.com/me> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/name> \"Pablo Orduña\" . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/title> \"Sr\" . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/givenname> \"Pablo\" . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/family_name> \"Orduña\" . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/homepage> <http://pablo.ordunya.com> . \n" +
			"<http://pablo.ordunya.com/me> <http://xmlns.com/foaf/0.1/depiction> <http://paginaspersonales.deusto.es/porduna/images/porduna.png> . \n",
			SemanticFormat.NTRIPLES);
	
	public static final Graph YODA_GRAPH = new Graph(
			"<http://facebook.com/user/yoda> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/name> \"Yoda\" . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/title> \"Jedi\" . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/givenname> \"Yoda\" . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/homepage> <http://yodaknowsit.com> . \n" +
			"<http://facebook.com/user/yoda> <http://xmlns.com/foaf/0.1/depiction> <http://upload.wikimedia.org/wikipedia/en/9/96/CGIYoda.jpg> . \n",
			SemanticFormat.NTRIPLES);
	
	
	public OtsoServerManager(int otsoTestingPort){
		this(otsoTestingPort, null);
	}
	
	public OtsoServerManager(int otsoTestingPort, ICommunication multicastProvider){
		this(otsoTestingPort, multicastProvider, true);
	}
	
	public OtsoServerManager(int otsoTestingPort, ICommunication multicastProvider, boolean provideController){
		this.otsoTestingPort = otsoTestingPort;
		if(provideController){
			this.controller = EasyMock.createMock(IController.class);
			//EasyMock.expect(this.controller.getDataAccessService()).andReturn(new FakeDataAccess()).anyTimes();
			EasyMock.expect(this.controller.getDataAccessService()).andReturn(new MemoryDataAccess()).anyTimes();
			EasyMock.replay(this.controller);
		}else
			this.controller = null;
		
		this.rs = new OtsoRestServer(otsoTestingPort, this.controller, multicastProvider);
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
