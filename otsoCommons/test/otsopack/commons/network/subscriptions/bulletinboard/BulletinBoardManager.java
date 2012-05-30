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
package otsopack.commons.network.subscriptions.bulletinboard;

import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;

import otsopack.commons.IController;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.communication.OtsoRestServer;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.IRegistryManager;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.registry.RegistryException;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class BulletinBoardManager {
	protected String defaultSpace = "http://default/";
	
	private BulletinBoardsManager server;
	protected Set<Node> otherBulletinBoards = new HashSet<Node>();
	
	private Set<OtsoRestServer> remoteListeners = new HashSet<OtsoRestServer>();
	private int bbPort;
	private int clientPort;
	
	public BulletinBoardManager(int port){
		this(port, OtsoRestServer.DEFAULT_PORT);
	}
	
	public BulletinBoardManager(int port, int clientPort) {
		this.bbPort = port;
		this.clientPort = clientPort;
	}
	
	public void start() throws Exception {
		final IRegistry registry = EasyMock.createMock(IRegistry.class);
		EasyMock.expect(registry.getBulletinBoards(this.defaultSpace)).andReturn(otherBulletinBoards).anyTimes();
		EasyMock.replay(registry);
		
		// this info holder is for the listener of the server itself, but we won't 
		this.server = new BulletinBoardsManager(registry, new InfoHolder(this.bbPort));
		this.server.startup();
		this.server.createRemoteBulletinBoard(this.defaultSpace, this.bbPort);
	}
	
	public IBulletinBoard createClient() throws Exception {
		this.clientPort++;
		
		final BulletinBoardsManager bbm = new BulletinBoardsManager(new FakeRegistry(this.bbPort), new InfoHolder(this.clientPort));
		bbm.joinToRemoteBulletinBoard(this.defaultSpace);
		
		final IController controller = EasyMock.createMock(IController.class);
		EasyMock.expect(controller.getSubscriber()).andReturn(bbm).anyTimes();
		EasyMock.replay(controller);
		
		final OtsoRestServer listnr = new OtsoRestServer(this.clientPort, controller, null);
		listnr.startup();
		remoteListeners.add(listnr);
		
		return bbm.getBulletinBoard(this.defaultSpace);
	}

	public String createClientAddress() {
		return "http://127.0.0.1:" + this.clientPort;
	}
	
	public void stop() throws Exception {
		for(OtsoRestServer svr: this.remoteListeners) {
			svr.shutdown();
		}
		this.server.shutdown();
	}
	
	public IBulletinBoard getServer() {
		return this.server.getBulletinBoard(this.defaultSpace);
	}
	
	public void addOtherBulletinBoard(Node node) {
		this.otherBulletinBoards.add(node);
	}
}

// TODO mock
class FakeRegistry implements IRegistryManager {
	int port;
	
	public FakeRegistry(int port) {
		this.port = port;
	}
	
	@Override
	public Set<ISpaceManager> getSpaceManagers(String spaceURI) {
		return null;
	}
	@Override
	public Set<Node> getNodesBaseURLs(String spaceURI) {
		return null;
	}
	@Override
	public Set<Node> getBulletinBoards(String spaceURI) {
		final Set<Node> bbs = new HashSet<Node>();
		bbs.add(new Node("http://localhost:" + this.port + OtsopackHttpBulletinBoardProviderApplication.BULLETIN_ROOT_PATH,
						"bboard0", true, true, false));
		return bbs;
	}
	@Override
	public void startup() throws RegistryException {
	}
	@Override
	public void shutdown() throws RegistryException {
	}
	@Override
	public void joinSpace(String spaceURI) {
	}
	@Override
	public void leaveSpace(String spaceURI) {
	}
}

class InfoHolder implements IHTTPInformation {
	private int port;
	public InfoHolder(int port) {
		this.port = port;
	}
	@Override
	public String getAddress() {
		return "http://localhost";
	}
	@Override
	public int getPort() {
		return this.port;
	}
}