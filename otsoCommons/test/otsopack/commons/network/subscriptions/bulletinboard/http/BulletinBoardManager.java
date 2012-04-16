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
import otsopack.commons.network.subscriptions.bulletinboard.http.server.BulletinBoardRestServer;

public class BulletinBoardManager {
	private BulletinBoardRestServer server;
	private int port;
	
	public BulletinBoardManager(int port){
		this.port = port;
	}
	
	public void start() throws Exception {
		final Node[] nodes = new Node[0];
		final SimpleRegistry registry = new SimpleRegistry("http://space", nodes);
		
		// TODO create registry
		this.server = new BulletinBoardRestServer(this.port, registry);
		this.server.startup();
	}
	
	public RemoteBulletinBoard createClient(){
		return new RemoteBulletinBoard(createClientAddress(), new FakeRegistry());
	}

	public String createClientAddress() {
		return "http://127.0.0.1:" + this.port;
	}
	
	public void stop() throws Exception {
		this.server.shutdown();
	}
	
	protected Collection<Subscription> getSubscriptions() {
		return ((LocalBulletinBoard)this.server.getApplication().getController().getBulletinBoard()).getSubscriptions();
	}
	
	// TODO mock
	class FakeRegistry implements IRegistryManager {
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
			bbs.add(new Node("http://localhost:"+port, "bboard0", true, true, false));
			return bbs;
		}
		@Override
		public void startup() throws RegistryException {
		}
		@Override
		public void shutdown() throws RegistryException {
		}
	}
}