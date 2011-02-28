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
package otsopack.otsoME.network.coordination;

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.exception.PeerGroupException;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import otsopack.otsoME.configuration.JxmeConfiguration;
import otsopack.otsoME.network.communication.ISpaceInformationHolder;
import otsopack.otsoME.network.communication.SpaceManager;
import otsopack.otsoME.network.jxme.JxmePeerBase;
import otsopack.commons.IController;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICoordination;
import otsopack.commons.util.Util;
import otsopack.commons.util.collections.HashMap;
import otsopack.commons.util.collections.Map;
import otsopack.commons.util.collections.Set;


public class JxmeCoordination implements ICoordination {
	private final static Logger LOG = Logger.getInstance(JxmeCoordination.class.getName());
	
	private IController controller;
	
	private JxmePeerBase jxmePeer;
	
	private Map/*<URI,SpaceManager>*/ spaces = null;
	private Map/*<URI,SpaceManager>*/ joinedSpaces = null;
           
	public JxmeCoordination(IController controller) {
		this.controller = controller;
	}
	
	public void startup() throws TSException {
		spaces = new HashMap();
		joinedSpaces = new HashMap();

		try {
			jxmePeer = new JxmePeerBase();
			jxmePeer.startJXTA();
			jxmePeer.waitForRendezvous(JxmeConfiguration.getConfiguration().getRendezvousConnectionTimeout());			
		}
		catch (PeerGroupException e) {
			throw new TSException(e.getMessage());			
		}		
	}
	
	public void shutdown() {
		Enumeration joinedEnum = joinedSpaces.values().elements();
		
		while( joinedEnum.hasMoreElements() ) {
			SpaceManager space = (SpaceManager) joinedEnum.nextElement();
			space.shutdown();
		}
		
		spaces.clear();
		joinedSpaces.clear();		
		spaces = null;		
		joinedSpaces = null;		
		
		jxmePeer.stopJXTA();
	}
		
   private ISpaceInformationHolder discoverSpace(String uri) {
	   if (this.spaces.containsKey(uri))
		   return (ISpaceInformationHolder) this.spaces.get(uri);
	   
	   //Search input pipe
	   //PipeAdvertisement pipeAdvertisement = jxmePeer.discoverPipe(uri);		   
	   
	   LOG.log(Priority.INFO, "Searching space called: " + uri);
	   
	   return null;
   }

	public void createSpace(String spaceURI) throws TSException {
		ISpaceInformationHolder newSpace = this.discoverSpace(spaceURI);
		
		if (newSpace == null) {
			newSpace = new SpaceManager(controller, spaceURI, jxmePeer.getNetPeerGroup());			
		}
		
		spaces.put(spaceURI,newSpace);
		
		LOG.log(Priority.DEBUG, "Group created: " + spaceURI);
	}

    /* how many pipes the peer is listening to affects to the speed in which
     * they are able to poll in each of them
     */
	public void joinSpace(String spaceURI) throws TSException {			
		if( !spaces.containsKey(spaceURI) ) throw new SpaceNotExistsException();
		
		SpaceManager space = (SpaceManager) this.spaces.get(spaceURI);
		
		try {
			space.startup();
		} 
		catch (IOException e) {
			throw new TSException(e.getMessage());
		}

		joinedSpaces.put(spaceURI, space);	
		LOG.log(Priority.DEBUG, "Joined to group: " + spaceURI);
	}

	public void leaveSpace(String spaceURI) {
		if( joinedSpaces.containsKey(spaceURI) ) {
			SpaceManager js = (SpaceManager)joinedSpaces.get(spaceURI);
			js.shutdown();
			
			joinedSpaces.remove(spaceURI);			
		}
	}
	
	public SpaceManager getSpace(String spaceURI) throws SpaceNotExistsException {
		SpaceManager ret = null;
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		ret = (SpaceManager)joinedSpaces.get(spaceURI);
		if( ret==null ) throw new SpaceNotExistsException();
		return ret;
	}
	
	public String getID() {
		if (jxmePeer.getNetPeerGroup() != null) {
			return jxmePeer.getNetPeerGroup().getPeerID().toString();
		}
		return null;
	}

	public Set getJoinedSpaces() {
		return joinedSpaces.keySet();
	}

	public Set getSpaces() {
		return spaces.keySet();
	}

	public String getPeerName() {
		return jxmePeer.getNetPeerGroup().getPeerName();
	}
}