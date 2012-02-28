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

import otsopack.commons.IController;
import otsopack.commons.authz.Filter;
import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.network.communication.OtsoRestServer;
import otsopack.commons.network.communication.RestMulticastCommunication;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.network.coordination.IRegistry;
import otsopack.commons.network.coordination.bulletinboard.BulletinBoardsManager;

public class RestNetwork implements INetwork {
	
	OtsoRestServer rs;
	private ICommunication comm;
	private Set<String> joinedSpaces = new CopyOnWriteArraySet<String>();
	private BulletinBoardsManager bulletinBoards;
	
	public RestNetwork(IController controller) {
		this.rs = new OtsoRestServer();
		this.rs.getApplication().setController(controller);
	}

	public RestNetwork(IController controller, int port, IEntity signer, IRegistry registry, BulletinBoardsManager bbMngr) {
		this.comm = new RestMulticastCommunication(registry);
		this.bulletinBoards = bbMngr;
		this.rs = new OtsoRestServer(port, controller, signer/*, bbMngr*/);
		this.rs.getApplication().setController(controller);
	}
	
	public OtsoRestServer getRestServer() {
		return this.rs;
	}

	@Override
	public void startup() throws TSException {
		try {
			this.comm.startup();
			this.rs.startup();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TSException("Rest server could not be started. " + e.getMessage());
		}
		this.joinedSpaces.clear();
	}

	@Override
	public void shutdown() throws TSException {
		try {
			this.comm.shutdown();
			this.rs.shutdown();
		} catch (Exception e) {
			throw new TSException("Rest server could not be restarted. " + e.getMessage());
		}
	}
	
	@Override
	public Graph read(String spaceURI, String graphURI,	SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return this.comm.read(spaceURI, graphURI, outputFormat, filters, timeout);
	}
	
	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return this.comm.read(spaceURI, graphURI, outputFormat, timeout);
	}
	
	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.read(spaceURI, template, outputFormat, filters, timeout);
	}
	
	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat,long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.read(spaceURI, template, outputFormat, timeout);
	}
	
	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return this.comm.take(spaceURI, graphURI, outputFormat, filters, timeout);
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		return this.comm.take(spaceURI, graphURI, outputFormat, timeout);
	}
	
	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.take(spaceURI, template, outputFormat, filters, timeout);
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.take(spaceURI, template, outputFormat, timeout);
	}
	
	@Override
	public Graph [] query(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.query(spaceURI, template, outputFormat, filters, timeout);
	}

	@Override
	public Graph [] query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException, UnsupportedSemanticFormatException {
		return this.comm.query(spaceURI, template, outputFormat, timeout);
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
	public String advertise(String spaceURI, NotificableTemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unadvertise(String spaceURI, String advertisementURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getJoinedSpaces() {
		return this.joinedSpaces;
	}

	@Override
	public void createSpace(String spaceURI) throws TSException {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICoordination getCoordination() {
		// TODO Auto-generated method stub
		return null;
	}
}
