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
import otsopack.commons.exceptions.SubscriptionException;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.communication.OtsoRestServer;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class BulletinBoardManager {
	protected String defaultSpace = "http://default/";
	
	private BulletinBoardsManager server;
	protected Set<Node> registeredBulletinBoards = new HashSet<Node>();
	
	private Set<OtsoRestServer> remoteListeners = new HashSet<OtsoRestServer>();
	
	private String nodeUuid;
	private int bbPort;
	private int clientPort;
	
	public BulletinBoardManager(String nodeUuid, int port){
		this(nodeUuid, port, OtsoRestServer.DEFAULT_PORT);
	}
	
	public BulletinBoardManager(String nodeUuid, int port, int clientPort) {
		this.nodeUuid = nodeUuid;
		this.bbPort = port;
		this.clientPort = clientPort;
	}
	
	public void start() throws Exception {
		createDefaultBulletinBoard();
	}
	
	private void createDefaultBulletinBoard() throws SubscriptionException {
		final IRegistry registry = EasyMock.createMock(IRegistry.class);
		EasyMock.expect(registry.getBulletinBoards(this.defaultSpace)).andReturn(this.registeredBulletinBoards).anyTimes();
		EasyMock.expect(registry.getLocalUuid()).andReturn(this.nodeUuid).anyTimes();
		EasyMock.replay(registry);
		
		// this info holder is for the listener of the server itself, but we won't 
		this.server = new BulletinBoardsManager(registry, getHttpInformation(this.bbPort));
		this.server.startup();
		this.server.createRemoteBulletinBoard(this.defaultSpace, this.bbPort);
	}
	
	private IHTTPInformation getHttpInformation(int port) {
		final IHTTPInformation registry = EasyMock.createMock(IHTTPInformation.class);
		EasyMock.expect(registry.getAddress()).andReturn("http://localhost").anyTimes();
		EasyMock.expect(registry.getPort()).andReturn(port).anyTimes();
		EasyMock.replay(registry);
		return registry;
	}
	
	private IRegistry getRegistry() {
		final IRegistry registry = EasyMock.createMock(IRegistry.class);
		
		final Set<Node> bbs = new HashSet<Node>();
		bbs.add(new Node("http://localhost:" + this.bbPort + OtsopackHttpBulletinBoardProviderApplication.BULLETIN_ROOT_PATH,
							this.nodeUuid, true, true, false));
		EasyMock.expect(registry.getBulletinBoards(this.defaultSpace)).andReturn(bbs).anyTimes();
		EasyMock.expect(registry.getLocalUuid()).andReturn("node-"+this.bbPort).anyTimes();
		EasyMock.replay(registry);
		return registry;
	}
	
	public IBulletinBoard createClient() throws Exception {
		this.clientPort++;
		
		final BulletinBoardsManager bbm = new BulletinBoardsManager(getRegistry(), getHttpInformation(this.clientPort));
		bbm.joinToRemoteBulletinBoard(this.defaultSpace);
		
		final IController controller = EasyMock.createMock(IController.class);
		EasyMock.expect(controller.getSubscriber()).andReturn(bbm).anyTimes();
		EasyMock.replay(controller);
		
		final OtsoRestServer listnr = new OtsoRestServer(this.clientPort, controller, null);
		listnr.startup();
		this.remoteListeners.add(listnr);
		
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
		this.registeredBulletinBoards.add(node);
	}
}