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
package otsopack.full.java.network;

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
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.ICoordination;
import otsopack.commons.network.INetwork;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.util.collections.HashSet;
import otsopack.commons.util.collections.Set;
import otsopack.full.java.network.communication.OtsoRestServer;
import otsopack.full.java.network.communication.RestMulticastCommunication;
import otsopack.full.java.network.coordination.IBulletinBoard;
import otsopack.full.java.network.coordination.IRegistry;

public class RestNetwork implements INetwork {
	
	OtsoRestServer rs;
	private ICommunication comm;
	private Set/*<String>*/ joinedSpaces = new HashSet/*<String>*/();
	private IBulletinBoard bulletinBoard;
	
	public RestNetwork(IController controller) {
		this.rs = new OtsoRestServer();
		this.rs.getApplication().setController(controller);
	}

	public RestNetwork(IController controller, int port, IEntity signer, IRegistry registry, IBulletinBoard bulletinboard) {
		this.comm = new RestMulticastCommunication(registry);
		this.bulletinBoard = bulletinboard;
		this.rs = new OtsoRestServer(port, controller, signer, bulletinboard);
		this.rs.getApplication().setController(controller);
	}

	@Override
	public void startup() throws TSException {
		try {
			this.comm.startup();
			this.rs.startup();
		} catch (Exception e) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub

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
	public void demand(String spaceURI, Template template, long leaseTime,
			ISuggestionCallback callback) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void suggest(String spaceURI, Graph graph) throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean callbackIfIHaveResponsabilityOverThisKnowlege(
			String spaceURI, Graph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAnyPeerResponsabilityOverThisKnowlege(String spaceURI,
			Graph triples) throws TSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set getSpaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getJoinedSpaces() {
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
