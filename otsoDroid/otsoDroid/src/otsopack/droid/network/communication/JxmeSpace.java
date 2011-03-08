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
package otsopack.droid.network.communication;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import otsopack.commons.exceptions.MalformedMessageException;
import otsopack.commons.log.Printer;
import otsopack.commons.util.Util;
import otsopack.droid.network.communication.incoming.ITSCallback;
import otsopack.droid.network.communication.util.MessageParser;
import otsopack.droid.network.communication.util.MessageParser.Properties;

public class JxmeSpace implements PipeMsgListener, IMessageSender {	
	private final static Logger log = Logger.getLogger(JxmeSpace.class.getName());
	
	private PeerGroup peerGroup;
	private String spaceURI = null;
    private Vector<ITSCallback> listeners = null;

	private InputPipe input;
	private OutputPipe output;
    
    public JxmeSpace(PeerGroup ngroup, String spaceURI) {
    	this.peerGroup = ngroup;
    	this.spaceURI = spaceURI;
    	this.listeners = new Vector<ITSCallback>();
    }
    
	private void createPipe() throws IOException {
		log.debug(Level.INFO, "Looking up (" + this.spaceURI + ") pipe.");
		
		PipeAdvertisement pipeAdvertisement = this.searchPipeAdvertisement(this.spaceURI);
		
		if (pipeAdvertisement == null) {
			log.debug(Level.INFO, "Creating pipe (" + this.spaceURI + ")");

			pipeAdvertisement = this.createPipeAdvertisement(this.peerGroup,this.spaceURI);		
			
			try {
				log.debug(Level.INFO, "Publishing pipe (" + this.spaceURI + ")");
				
				DiscoveryService discoveryService = peerGroup.getDiscoveryService();
				discoveryService.publish(pipeAdvertisement);
				discoveryService.remotePublish(pipeAdvertisement,DiscoveryService.DEFAULT_EXPIRATION);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			log.debug(Level.INFO, "Pipe (" + this.spaceURI + ") found.");
		}
		
		int timeout = 100;
		
		// Create the input pipe with this app as the message listener for this
		input = peerGroup.getPipeService().createInputPipe(pipeAdvertisement, this);
		
		// This pipe is a propagated pipe, therefore also bind to it
		output = peerGroup.getPipeService().createOutputPipe(pipeAdvertisement, timeout);
		
		log.debug("Pipes created for space " + spaceURI);
	}
    
	private PipeAdvertisement searchPipeAdvertisement(String pipeName) {
		PipeAdvertisement pipeAdvertisement = null;		
		
		String attribute = "Name";
		String value = pipeName;
		int threshold = 1;
			
		//Once the remote search is done, the local method should retrieve the group if found
		DiscoveryService discoveryService = this.peerGroup.getDiscoveryService();
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, attribute, value, threshold);
		
		try {
			Enumeration<?> advs = discoveryService.getLocalAdvertisements(DiscoveryService.ADV, attribute, value);
			if (advs.hasMoreElements()) {
				pipeAdvertisement = (PipeAdvertisement) advs.nextElement();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
				
		return pipeAdvertisement;
	}
	
	protected PipeAdvertisement createPipeAdvertisement(PeerGroup peerGroup, String pipeName) {
		//preCookedPID  = MessageDigest.getInstance("MD5").digest(spaceURI.getBytes());
		byte[] seed = null;
		try {
			seed = Util.md5( pipeName );
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		PipeID pipeID = IDFactory.newPipeID( peerGroup.getPeerGroupID(), seed );
		/*PipeID id = IDFactory.newPipeID( IDFactory.newPeerGroupID() );*/ // it generates different ID in different Peers
		
		PipeAdvertisement pipeAdv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		pipeAdv.setPipeID(pipeID);
		pipeAdv.setName(pipeName);
		pipeAdv.setType(PipeService.PropagateType);
		
		return pipeAdv;
	}
    
    public void startup() throws IOException {
		this.createPipe();
    }
    
    public void shutdown() {
    	input.close();
    	output.close();

    	input = null;
    	output = null;
    	listeners.removeAllElements();    	
    }
    
    	private boolean itIsMe(Message msg) {
        	if(msg != null) {
        		MessageElement msgElement = msg.getMessageElement(null,Properties.SENDER);
    			if( msgElement!=null ) {
    				String sender = msgElement.toString();
    				if( peerGroup!=null )
    					return sender.equals(  peerGroup.getPeerName() );
    			}
        	}
        	return false;
    	}
    
	public void pipeMsgEvent(PipeMsgEvent event) {
		final Message msg = event.getMessage();			
		
		if (msg == null) return;
		
		if( !itIsMe(msg) ) {
			new Thread(
		            new Runnable() {
			            public void run() {
			            	try {
				            	MessageParser.parseMessage(msg, listeners);
							} 
			            	catch (MalformedMessageException e) {
								Printer.err_println("Poll, Error processing message: " + e.getMessage());
							}
			            }
		            }).start();
		}
	}
	
	/* (non-Javadoc)
	 * @see es.deusto.tecnologico.tscME.network.communication.IMessageSender#send(net.jxta.endpoint.Message)
	 */
	public void send(Message msg) {    	
		if(msg!=null) {
			try {
				output.send(msg);
			} catch (IOException e) {
				Printer.err_println(e.getMessage());
			}
		}
	}
	
	public void addListener(ITSCallback listener) {
		this.listeners.addElement(listener);
	}
	
	public void removeListener(ITSCallback listener) {
		this.listeners.removeElement(listener);
	}

	public String getSpaceURI() {
		return spaceURI;
	}
}