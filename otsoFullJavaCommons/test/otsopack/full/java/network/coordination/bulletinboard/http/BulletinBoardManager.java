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
package otsopack.full.java.network.coordination.bulletinboard.http;

import java.util.Collection;

import otsopack.commons.data.WildcardTemplate;
import otsopack.full.java.network.coordination.Node;
import otsopack.full.java.network.coordination.bulletinboard.RemoteBulletinBoard;
import otsopack.full.java.network.coordination.bulletinboard.data.Advertisement;
import otsopack.full.java.network.coordination.bulletinboard.data.Subscription;
import otsopack.full.java.network.coordination.bulletinboard.http.server.BulletinBoardRestServer;
import otsopack.full.java.network.coordination.registry.SimpleRegistry;

public class BulletinBoardManager {
	public final Advertisement ADV1 = new Advertisement("adv1", System.currentTimeMillis()+3600000, WildcardTemplate.createWithURI("http://subj1","http://predicate1","http://obj1"));
	public final Advertisement ADV2 = new Advertisement("adv2", System.currentTimeMillis()+3600000, WildcardTemplate.createWithNull(null,"http://predicate2"));
	
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
		this.server.getApplication().getController().getBulletinBoard().advertise(this.ADV1);
		this.server.getApplication().getController().getBulletinBoard().advertise(this.ADV2);
		this.server.startup();
	}
	
	public RemoteBulletinBoard createClient(){
		return new RemoteBulletinBoard(createClientAddress());
	}

	public String createClientAddress() {
		return "http://127.0.0.1:" + this.port;
	}
	
	public void stop() throws Exception {
		this.server.shutdown();
	}
	
	protected Advertisement[] getAdvertisements() {
		return this.server.getApplication().getController().getBulletinBoard().getAdvertisements();
	}
	
	protected Collection<Subscription> getSubscriptions() {
		return this.server.getApplication().getController().getBulletinBoard().getSubscriptions();
	}
}