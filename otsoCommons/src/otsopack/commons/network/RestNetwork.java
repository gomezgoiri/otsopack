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
package otsopack.commons.network;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import otsopack.commons.Arguments;
import otsopack.commons.IController;
import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.network.communication.OtsoRestServer;
import otsopack.commons.network.communication.RestCommunicationException;
import otsopack.commons.network.communication.RestMulticastCommunication;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.coordination.IRegistryManager;
import otsopack.commons.network.coordination.registry.RegistryException;
import otsopack.commons.network.subscriptions.bulletinboard.BulletinBoardsManager;
import otsopack.commons.network.subscriptions.bulletinboard.IBulletinBoard;

public class RestNetwork implements INetwork {
	
	OtsoRestServer rs;
	private IRegistryManager registry;
	private RestMulticastCommunication comm;
	private Set<String> joinedSpaces = new CopyOnWriteArraySet<String>();
	private BulletinBoardsManager bulletinBoards;
	

	public RestNetwork(IController controller, int port, IEntity signer, IRegistryManager registry) {
		this.registry = registry;
		this.comm = new RestMulticastCommunication(registry);
		this.rs = new OtsoRestServer(port, controller, signer);
		this.rs.getApplication().setController(controller);
		this.bulletinBoards = new BulletinBoardsManager(registry, this.rs);
	}
	
	public OtsoRestServer getRestServer() {
		return this.rs;
	}

	@Override
	public void startup() throws TSException {
		try {
			this.registry.startup();
			this.comm.startup();
			this.rs.startup();
			this.bulletinBoards.startup();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TSException("Rest server could not be started. " + e.getMessage());
		}
		this.joinedSpaces.clear();
		try{
			this.registry.shutdown();
		}catch(RegistryException re){
			re.printStackTrace();
			throw new RestCommunicationException("Could not shutdown " + RestMulticastCommunication.class.getName() + ": " + re.getMessage());
		}
	}

	@Override
	public void shutdown() throws TSException {
		try {
			this.registry.shutdown();
			this.comm.shutdown();
			this.rs.shutdown();
			this.bulletinBoards.shutdown();
		} catch (Exception e) {
			throw new TSException("Rest server could not be restarted. " + e.getMessage());
		}
	}
	
	@Override
	public Graph read(String spaceURI, String graphURI,	Arguments configuration) throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return this.comm.read(spaceURI, graphURI, configuration);
	}
	
	@Override
	public Graph read(String spaceURI, Template template, Arguments configuration) throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.read(spaceURI, template, configuration);
	}

	@Override
	public Graph take(String spaceURI, String graphURI, Arguments configuration) throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return this.comm.take(spaceURI, graphURI, configuration);
	}
	
	@Override
	public Graph take(String spaceURI, Template template, Arguments configuration) throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.take(spaceURI, template, configuration);
	}
	
	@Override
	public Graph [] query(String spaceURI, Template template, Arguments configuration) throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.query(spaceURI, template, configuration);
	}

	@Override
	public String subscribe(String spaceURI, NotificableTemplate template,
			INotificationListener listener) throws SpaceNotExistsException {
		// TODO check where to set the expiration time
		return this.bulletinBoards.subscribe(spaceURI, template, listener);
	}

	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI)
			throws SpaceNotExistsException {
		this.bulletinBoards.unsubscribe(spaceURI, subscriptionURI);
	}

	@Override
	public void notify(String spaceURI, NotificableTemplate template)
			throws SpaceNotExistsException {
		this.bulletinBoards.notify(spaceURI, template);
	}

	@Override
	public Set<String> getJoinedSpaces() {
		return this.joinedSpaces;
	}

	@Override
	public void joinSpace(String spaceURI) throws TSException {
		this.joinedSpaces.add(spaceURI);
	}

	@Override
	public void leaveSpace(String spaceURI) throws TSException {
		this.joinedSpaces.remove(spaceURI);
	}

	@Override
	public String getPeerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICommunication getCommunication() {
		return this.comm;
	}

	@Override
	public ICoordination getCoordination() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ISubscriptions getSubscriptions() {
		return this.bulletinBoards;
	}
	
	@Override
	public IBulletinBoard getBulletinBoard(String spaceURI) {
		return this.bulletinBoards.getBulletinBoard(spaceURI);
	}
}
