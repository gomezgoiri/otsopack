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
package otsopack.otsoME.network.communication.incoming;

import java.util.Enumeration;

import net.sf.microlog.core.Level;
import net.sf.microlog.core.LoggerFactory;
import net.sf.microlog.midp.appender.HttpAppender;

import org.apache.log4j.Logger;

import otsopack.commons.IController;
import otsopack.commons.data.Graph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.ResponseNotExpected;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.otsoME.network.communication.ISpaceInformationHolder;
import otsopack.otsoME.network.communication.demand.local.ISuggestionCallbackManager;
import otsopack.otsoME.network.communication.demand.remote.IRemoteDemandIOManager;
import otsopack.otsoME.network.communication.incoming.response.ModelResponse;
import otsopack.otsoME.network.communication.incoming.response.Response;
import otsopack.otsoME.network.communication.incoming.response.URIResponse;
import otsopack.otsoME.network.communication.notifications.INotificationChooser;
import otsopack.otsoME.network.communication.notifications.ISubscription;
import otsopack.otsoME.network.communication.outcoming.IResponseSender;


public class ResponseManager implements ITSCallback {
	private final static Logger log = Logger.getInstance(ResponseManager.class.getName());
	
	IController controller = null;
	IResponseSender outcoming;
	ISpaceInformationHolder spaceInfo;
	
	private IncomingList inbox = null;
	private IRemoteDemandIOManager demandInput;
	private INotificationChooser subscriberList;
	private ISuggestionCallbackManager suggestionCallback;
	
	public ResponseManager(IController control, IncomingList inbox,
			INotificationChooser subscriptions, IRemoteDemandIOManager demandInput,
			ISuggestionCallbackManager suggestionCallback, IResponseSender sender,
			ISpaceInformationHolder spaceInfo) {
		this.controller = control;
		this.inbox = inbox;
		this.subscriberList = subscriptions;
    	this.demandInput = demandInput;
    	this.suggestionCallback = suggestionCallback;
    	this.spaceInfo = spaceInfo;
    	this.outcoming = sender;
	}
	
	public void query(Template template) throws UnsupportedTemplateException {
    	log.debug("Query received");
    	try {
			Graph graph = controller.getDataAccessService().query(spaceInfo.getSpaceURI(), template, SemanticFormat.NTRIPLES);
			Graph resp = new ModelImpl(graph).write(SemanticFormat.NTRIPLES);
			if(resp!=null)
				outcoming.response(template, resp);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		} catch (UnsupportedSemanticFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void queryMultiple(Template[] templates) throws UnsupportedTemplateException {
		log.debug("QueryMultiple received");
		try {
			if( templates!=null ) {
				for( int i=0; i<templates.length; i++ ) {
					Graph graph = controller.getDataAccessService().query(spaceInfo.getSpaceURI(), templates[i], SemanticFormat.NTRIPLES);
					Graph resp = new ModelImpl(graph).write(SemanticFormat.NTRIPLES);
					if(resp!=null)						
						outcoming.response(templates[i], resp);
				}
			}
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		} catch (UnsupportedSemanticFormatException e) {
			e.printStackTrace();
		}
	}

	public void read(Template template) throws UnsupportedTemplateException {
    	log.debug("Read received.");
		try {
			Graph graph = controller.getDataAccessService().read(spaceInfo.getSpaceURI(), template, SemanticFormat.NTRIPLES);
			Graph resp = new ModelImpl(graph).write(SemanticFormat.NTRIPLES);
			if(resp!=null)
				outcoming.response(template, resp);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		} catch (UnsupportedSemanticFormatException e) {
			e.printStackTrace();
		}
	}

	public void read(String graphuri) {
    	log.debug("Read received.");
		try {
			Graph graph = controller.getDataAccessService().read(spaceInfo.getSpaceURI(), graphuri, SemanticFormat.NTRIPLES);
			Graph resp = new ModelImpl(graph).write(SemanticFormat.NTRIPLES);
			if(resp!=null)
				outcoming.response(graphuri, resp);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		} catch (UnsupportedSemanticFormatException e) {
			e.printStackTrace();
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
	}
	
	public void take(Template template) throws UnsupportedTemplateException {
		log.debug("Take received.");
		try {
			Graph graph = controller.getDataAccessService().take(spaceInfo.getSpaceURI(), template, SemanticFormat.NTRIPLES);
			Graph resp = new ModelImpl(graph).write(SemanticFormat.NTRIPLES);
			if(resp!=null)
				outcoming.response(template, resp);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		} catch (UnsupportedSemanticFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void take(String graphuri) {
		log.debug("Take received.");
		try {
			Graph graph = controller.getDataAccessService().take(spaceInfo.getSpaceURI(), graphuri, SemanticFormat.NTRIPLES);
			Graph resp = new ModelImpl(graph).write(SemanticFormat.NTRIPLES);
			if(resp!=null)
				outcoming.response(graphuri, resp);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		} catch (UnsupportedSemanticFormatException e) {
			e.printStackTrace();
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
	}
	
	public void advertise(String subscriptionURI) {
		log.debug("Advertise received");
		//the mobile peer does nothing, this message must not exist
		/*Subscription subsc =  space.getSubscription(subscriptionURI);
		if(subsc != null) subsc.getListener().notifyEvent();*/
	}
	
	public void notify(NotificableTemplate template) {
    	log.debug("Notify received");
		Enumeration enumeration = subscriberList.getThoseWhichMatch(template).elements();
		while( enumeration.hasMoreElements() ) {
			ISubscription subscription = (ISubscription) enumeration.nextElement();
			subscription.getListener().notifyEvent();
		}
	}
	
	public void advertise(NotificableTemplate template) {
		log.debug("Advertise received/done");
		//if we want to made mobile peers less dependent it is useful to handle this message
		//addressed to ProxyME peers
		notify(template); //TODO not advert to yourself!
	}
	
	public void unadvertise(String advertisementURI) {
		log.debug("Unadvertise not implemented");
		//the mobile peer does nothing, this is a message for the ProxyME peer
	}
	
	public void subscribe(NotificableTemplate template) {
		log.debug("Subscribe received");
		//the mobile peer does nothing, this is a message for the ProxyME peer 
	}

	public void unsubscribe(String subscriptionURI) {
		log.debug("Unsubscribe received");
		//the mobile peer does nothing, this is a message for the ProxyME peer
	}
	
	public void response(Template inResponseTo, IModel model) {
		log.debug("Response received to template "+inResponseTo);
		try {
			responseReceived(inResponseTo, model);
		} catch (ResponseNotExpected e) {
			log.debug(e.getMessage());
		}
	}
	
	public void response(String inResponseToGraphUri, IModel model) {
    	log.debug("Response received to requestId "+inResponseToGraphUri);
    	try {
    		responseReceived(inResponseToGraphUri, model);
    	} catch(ResponseNotExpected e) {
    		log.debug(e.getMessage());
    	}
	}

	public void response(Template inResponseToAdvSubs, String advSubsURI) {
		log.debug("Response received to advert or to a subscription "+inResponseToAdvSubs);
		try {
			responseReceived(inResponseToAdvSubs, advSubsURI);
		} catch (ResponseNotExpected e) {
			log.debug(e.getMessage());
		}
	}

	protected void responseReceived(Template inResponseTo, IModel model)
			throws ResponseNotExpected {
		Response resp = inbox.get(inResponseTo,false);
		if( resp == null ) 
			throw new ResponseNotExpected("Nobody is waiting to this response here.");
		
		((ModelResponse)resp).addTriples(model);
	}

	protected void responseReceived(String inResponseToGraphUri, IModel model) throws ResponseNotExpected {
        Response resp = inbox.get(inResponseToGraphUri,false);
        if( resp == null ) 
        	throw new ResponseNotExpected("Nobody is waiting to this response here.");
        
        ((ModelResponse)resp).addTriples(model);
	}

	protected void responseReceived(Template inResponseToAdvSubs, String advSubsURI)
			throws ResponseNotExpected {
		URIResponse resp = (URIResponse) inbox.get(inResponseToAdvSubs,true);
        if( resp == null ) 
        	throw new ResponseNotExpected("Nobody is waiting to this response here.");
        //it is not an error, is very usual
        resp.setURI(advSubsURI);
	}

	public void demand(Template template, long leaseTime) {
		//log();
		demandInput.demandReceived(template, leaseTime);
	}
	
	/*Useful for time measurements of demand primitive*/
	void log() {
		//Since we cannot configure the url in microlog.properties, we must do it programatically 
		HttpAppender appender = new HttpAppender();
		appender.setPostURL("http://192.168.2.104/postReceiver.php?file=cow");
		net.sf.microlog.core.Logger log2 = LoggerFactory.getLogger(ResponseManager.class.getName());
		log2.addAppender(appender);
		log2.setLevel(Level.DEBUG);
		log2.debug("Demand received on: "+System.currentTimeMillis());
	}

	public void suggest(IModel triples) throws UnsupportedTemplateException {
		suggestionCallback.callbackForMatchingTemplates( ((ModelImpl)triples).write(SemanticFormat.NTRIPLES) );
	}

	public void obtainDemands() {
		final byte[] exported = demandInput.exportRecords();
		if( exported!=null ) {
			outcoming.responseToObtainDemands(exported);
		}
	}

	public void responseDemands(byte[] bytes) {
		log.debug("BEGIN response to obtainDemands");
		if( !demandInput.hasBeenInitialized() ) {
			demandInput.importRecords(bytes);
		}
		log.debug("END response to obtainDemands");
	}
}