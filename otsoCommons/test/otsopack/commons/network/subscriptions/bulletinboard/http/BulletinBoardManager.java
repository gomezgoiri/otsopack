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

import org.easymock.EasyMock;

import otsopack.commons.IController;
import otsopack.commons.network.IHTTPInformation;
import otsopack.commons.network.communication.OtsoRestServer;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.IRegistryManager;
import otsopack.commons.network.coordination.ISpaceManager;
import otsopack.commons.network.coordination.Node;
import otsopack.commons.network.coordination.registry.RegistryException;
import otsopack.commons.network.subscriptions.bulletinboard.BulletinBoardsManager;
import otsopack.commons.network.subscriptions.bulletinboard.LocalBulletinBoard;
import otsopack.commons.network.subscriptions.bulletinboard.RemoteBulletinBoard;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.BulletinBoardRestServer;
import otsopack.commons.network.subscriptions.bulletinboard.http.server.provider.OtsopackHttpBulletinBoardProviderApplication;

public class BulletinBoardManager {
	protected String defaultSpace = "http://default";
	
	private BulletinBoardRestServer server;
	protected Set<Node> otherBulletinBoards = new HashSet<Node>();
	
	private Set<OtsoRestServer> remoteListeners = new HashSet<OtsoRestServer>();
	private int bbPort;
	private int clientPort;
	
	public BulletinBoardManager(int port){
		this(port, OtsoRestServer.DEFAULT_PORT);
	}
	
	public BulletinBoardManager(int port, int clientPort){
		this.bbPort = port;
		this.clientPort = clientPort;
	}
	
	public void start() throws Exception {
		final IRegistry registry = EasyMock.createMock(IRegistry.class);
		EasyMock.expect(registry.getBulletinBoards()).andReturn(otherBulletinBoards).anyTimes();
		EasyMock.replay(registry);
		
		this.server = new BulletinBoardRestServer(this.bbPort, registry);
		this.server.startup();
	}
	
	public RemoteBulletinBoard createClient() throws Exception{
		this.clientPort++;
		
		
		final BulletinBoardsManager bbm = new BulletinBoardsManager(new FakeRegistry(this.bbPort), new InfoHolder(this.clientPort));
		bbm.createBulletinBoard(defaultSpace);
		
		final IController controller = EasyMock.createMock(IController.class);
		EasyMock.expect(controller.getSubscriber()).andReturn(bbm).anyTimes();
		EasyMock.replay(controller);
		
		final OtsoRestServer listnr = new OtsoRestServer(this.clientPort, controller, null);
		listnr.startup();
		remoteListeners.add(listnr);
		
		return (RemoteBulletinBoard)bbm.getBulletinBoard(defaultSpace);
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
}

class InfoHolder implements IHTTPInformation {
	private int port;
	public InfoHolder(int port) {
		this.port = port;
	}
	@Override
	public String getAddress() {
		return "http://localhost:" + this.port;
	}
	@Override
	public int getPort() {
		return this.port;
	}
}