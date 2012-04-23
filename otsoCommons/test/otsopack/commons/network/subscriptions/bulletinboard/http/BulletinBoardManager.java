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
package otsopack.commons.network.subscriptions.bulletinboard.http;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import otsopack.commons.network.coordination.IRegistryManager;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.registry.RegistryException;
import otsopack.commons.network.coordination.registry.SimpleRegistry;
import otsopack.commons.network.subscriptions.bulletinboard.LocalBulletinBoard;
import otsopack.commons.network.subscriptions.bulletinboard.RemoteBulletinBoard;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.BulletinBoardController;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.BulletinBoardListenerRestServer;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.BulletinBoardRestServer;

public class BulletinBoardManager {
	private BulletinBoardRestServer server;
	private Set<BulletinBoardListenerRestServer> remoteListeners = new HashSet<BulletinBoardListenerRestServer>();
	private int bbPort;
	private int clientPort = BulletinBoardListenerRestServer.DEFAULT_PORT;
	
	public BulletinBoardManager(int port){
		this.bbPort = port;
	}
	
	public void start() throws Exception {
		final Node[] nodes = new Node[0];
		final SimpleRegistry registry = new SimpleRegistry("http://space", nodes);
		
		// TODO create registry
		this.server = new BulletinBoardRestServer(this.bbPort, registry);
		this.server.startup();
	}
	
	public RemoteBulletinBoard createClient() throws Exception{
		this.clientPort++;
		
		final RemoteBulletinBoard bb = new RemoteBulletinBoard(
												createClientAddress(),
												new FakeRegistry(this.bbPort) );
		final BulletinBoardListenerRestServer listnr = new BulletinBoardListenerRestServer(this.clientPort, new BulletinBoardController(bb));
		listnr.startup();
		remoteListeners.add(listnr);
		return bb;
	}

	public String createClientAddress() {
		return "http://127.0.0.1:" + this.clientPort;
	}
	
	public void stop() throws Exception {
		for(BulletinBoardListenerRestServer svr: this.remoteListeners) {
			svr.shutdown();
		}
		this.server.shutdown();
	}
	
	protected Collection<Subscription> getSubscriptions() {
		return ((LocalBulletinBoard)this.server.getApplication().getController().getBulletinBoard()).getSubscriptions();
	}
}

// TODO mock
class FakeRegistry implements IRegistryManager {
	int port;
	
	public FakeRegistry(int port) {
		this.port = port;
	}
	
	@Override
	public Set<ISpaceManager> getSpaceManagers() {
		return null;
	}
	@Override
	public Set<Node> getNodesBaseURLs() {
		return null;
	}
	@Override
	public Set<Node> getBulletinBoards() {
		final Set<Node> bbs = new HashSet<Node>();
		bbs.add(new Node("http://localhost:"+this.port, "bboard0", true, true, false));
		return bbs;
	}
	@Override
	public void startup() throws RegistryException {
	}
	@Override
	public void shutdown() throws RegistryException {
	}
}