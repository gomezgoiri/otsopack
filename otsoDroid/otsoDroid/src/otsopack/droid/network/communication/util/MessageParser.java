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
package otsopack.droid.network.communication.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import otsopack.commons.data.Graph;
import otsopack.commons.data.IModel;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.exceptions.MalformedMessageException;
import otsopack.commons.exceptions.MalformedTemplateException;
import otsopack.commons.exceptions.UnrecognizedFormatException;
import otsopack.commons.util.collections.HashSet;
import otsopack.droid.network.communication.incoming.ITSCallback;

public class MessageParser {
	public class TypeRequest {
		static public final String QUERY = "TSCQuery";
		static public final String READ = "TSCRead";
		static public final String TAKE = "TSCTake";
		static public final String ADVERTISE = "TSCAdvertise";
		static public final String UNADVERTISE = "TSCUnadvertise";
		static public final String SUBSCRIBE = "TSCSubscribe";
		static public final String UNSUBSCRIBE = "TSCUnsubscribe";
		static public final String NOTIFY = "TSCNotify";
		static public final String WRITE = "TSCWrite";
		static public final String RESPONSE = "TSCResponse";
		static public final String CREATE_GROUP = "TSCCreateGroup";
		static public final String LEAVE_GROUP = "TSCLeaveGroup";
		static public final String QUERYMULTIPLE = "TSCQueryMultiple";
		static public final String DEMAND = "TSCDemand";
		static public final String SUGGEST = "TSCSuggest";
		static public final String OBTAIN_DMNDS = "TSCObtainDemands";
		static public final String RESPONSE_DMNDS = "TSCResponseDemands";
	}
	
	public class Properties {
		//Propiedades que se envían
		static public final String REQUESTYPE = "TSCRequestType";
		static public final String PEERID = "JxtaPeerURN";
		static public final String SENDER = "JxtaSenderName";
		static public final String GROUP = "TSCGroupName";
		static public final String PEERNAMEQUERY = "JxtaQueryPeername";
		static public final String PEERNAMERESPONSE = "JxtaResponsePeername";		
		static public final String MODEL = "JxtaTalkSenderModel";
		static public final String GRAPHURI = "TSCGraphUri";
		static public final String TEMPLATEURI = "TSCSubscriptAdvertismnt";
		static public final String TEMPLATE = "TSCTemplateValue";
		static public final String QUERYMULTIPLE = "TSCQueryMultiple";
		static public final String TEMPLATE_MULTIPLE = "TSCTemplateMultiple";
		static public final String LEASE_TIME = "TSCLeaseTime";
		static public final String BYTES = "TSCBytes"; //for obtain demands
	}
	
    static public void parseMessage(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
    	if(msg != null) {
    		MessageElement msgElement = msg.getMessageElement(null,Properties.REQUESTYPE);
			if( msgElement!=null ) {
	        	String type = msgElement.toString();
		    	if(TypeRequest.RESPONSE.equals(type)) {
		    		parseResponse(msg, listeners);
		    	} else
		    	if(TypeRequest.QUERY.equals(type)) {
		    		parseQuery(msg, listeners);
		    	} else
	    		if(TypeRequest.QUERYMULTIPLE.equals(type)) {
		    		parseQueryMultiple(msg, listeners);
		    	} else
			    if(TypeRequest.READ.equals(type)) {
			    	parseRead(msg, listeners);
			    } else
			    if(TypeRequest.TAKE.equals(type)) {
			    	parseTake(msg, listeners);
			    } else
			    if(TypeRequest.ADVERTISE.equals(type)) {
			    	parseAdvertise(msg, listeners);
			    } else
				if(TypeRequest.UNADVERTISE.equals(type)) {
				    parseUnadvertise(msg, listeners);
				} else
			    if(TypeRequest.SUBSCRIBE.equals(type)) {
			    	parseSubscribe(msg, listeners);
			    } else
				if(TypeRequest.UNSUBSCRIBE.equals(type)) {
				    parseUnsubscribe(msg, listeners);
				} else
				if(TypeRequest.DEMAND.equals(type)) {
					parseDemand(msg, listeners);
				} else
				if(TypeRequest.SUGGEST.equals(type)) {
					parseSuggest(msg, listeners);
				} else
				if(TypeRequest.OBTAIN_DMNDS.equals(type)) {
					for(int i=0; i<listeners.size(); i++)
		    			listeners.elementAt(i).obtainDemands();
				} else
				if(TypeRequest.RESPONSE_DMNDS.equals(type)) {
					parseResponseDemands(msg, listeners);
				} else throw new UnrecognizedFormatException();
	        } else throw new MalformedMessageException();
    	} else throw new MalformedMessageException();
	}
    
   	static private ITemplate parseSelector(Message msg) throws MalformedMessageException {
    	final MessageElement msgElement = msg.getMessageElement(null,Properties.TEMPLATE);
    	if( msgElement!=null ) {
    		try {
				return new SemanticFactory().createTemplate(msgElement.toString());
			} catch (MalformedTemplateException e) {
				throw new MalformedMessageException(e.getMessage());
			}
    	}
    	throw new MalformedMessageException("Template was not sent");
	}
   	
   	static private IModel parseModel(Message msg) throws MalformedMessageException {
   		IModel ret = null;
   		final MessageElement msgElement = msg.getMessageElement(null,Properties.MODEL);
    	if( msgElement!=null ) {
        		ret = new ModelImpl();
        		final String data = new String(msgElement.getBytes(true));
    	        ret.read(new Graph(data, SemanticFormat.NTRIPLES));
    	}
    	return ret;
   	}
    
    static private void parseResponse(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
    	final IModel mod;
    	ITemplate sel;
    	String templateuri = null;
    	String graphuri = null;
    	
    	MessageElement msgElement = msg.getMessageElement(null,Properties.GRAPHURI);
    	if( msgElement!=null ) graphuri = msgElement.toString();
    	
    	mod = parseModel(msg);
    
    	msgElement = msg.getMessageElement(null,Properties.TEMPLATEURI);
    	if( msgElement!=null ) templateuri = msgElement.toString();
    	
    	try {
    		sel = parseSelector(msg);
    	} catch(MalformedMessageException mme) {
    		sel = null;
    	}    	
        
    	if( graphuri!=null ) {
    		for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).response(graphuri, mod);
        } else if( templateuri!=null) {
        	for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).response(sel, templateuri);
        } else if( sel!=null && mod!=null) {
        	for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).response(sel, mod);
        } else throw new MalformedMessageException();
    }
    
    static private void parseQuery(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {    	
   		final ITemplate selector = parseSelector(msg);
   		for(int i=0; i<listeners.size(); i++)
			listeners.elementAt(i).query(selector);
    }
    
    static private void parseQueryMultiple(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
    	try {
    		HashSet templates = new HashSet();
    		
    		MessageElement msgElement = msg.getMessageElement(null,Properties.TEMPLATE_MULTIPLE);
    		BufferedReader br = new BufferedReader( new StringReader(msgElement.toString()) );
			String line;
			try {
				while ((line = br.readLine()) != null) { 
					if(line.trim().compareTo("") != 0){
						ITemplate sel = null;
						try {
							sel = new SemanticFactory().createTemplate(line);
						} catch (MalformedTemplateException e) {
							//bad treatment, but it should never happen
							throw new RuntimeException(e.getMessage());
						}
						templates.add(sel);
					}
				}
				br.close();
			} catch (IOException e) {
				throw new MalformedMessageException(e.getMessage());
			}
			
			ITemplate[] sels = new ITemplate[templates.size()];
			Enumeration<?> it = templates.elements();
			for( int i=0; it.hasMoreElements(); i++ ) {
				sels[i] = (ITemplate) it.nextElement();
			}
			for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).queryMultiple(sels);
    	} catch (MalformedMessageException e) {
    		throw new MalformedMessageException(e.toString());
		}
    }
    
    static private void parseRead(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
    	MessageElement msgElement = msg.getMessageElement(null,Properties.GRAPHURI);
    	if( msgElement!=null ) {
    		for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).read( msgElement.toString() );
    	} else {
        	ITemplate selector = parseSelector(msg);
        	for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).read(selector);
        }
    }
    
    static private void parseTake(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
    	MessageElement msgElement = msg.getMessageElement(null,Properties.GRAPHURI);
    	if( msgElement!=null ) {
    		for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).take( msgElement.toString() );
        } else {
        	ITemplate selector = parseSelector(msg);
        	for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).take(selector);
        }
    }
    
    /*static private void parseNotify(Message msg, ITSCallback l) throws MalformedMessageException {
    	ITemplate selector = parseSelector(msg);
    	l.notify(selector);
    }*/    
    
    static private void parseAdvertise(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
    	final ITemplate selector = parseSelector(msg);
    	for(int i=0; i<listeners.size(); i++)
			listeners.elementAt(i).advertise(selector);
    }
    
    static private void parseUnadvertise(Message msg, Vector<ITSCallback> listeners) {
    	MessageElement msgElement = msg.getMessageElement(null,Properties.TEMPLATEURI);
    	if( msgElement!=null ) {
    		for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).unadvertise( msgElement.toString() );
        }
    }
    
    static private void parseSubscribe(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
        final ITemplate selector = parseSelector(msg);
        for(int i=0; i<listeners.size(); i++)
			listeners.elementAt(i).subscribe(selector);
    }
    
    static private void parseUnsubscribe(Message msg, Vector<ITSCallback> listeners) {
    	MessageElement msgElement = msg.getMessageElement(null,Properties.TEMPLATEURI);
    	if ( msgElement!=null ) {
    		for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).unsubscribe( msgElement.toString() );
        }
    }
    
    static private void parseDemand(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
    	final ITemplate selector = parseSelector(msg);
    	final MessageElement msgElement = msg.getMessageElement(null,Properties.LEASE_TIME);
    	if ( msgElement!=null ) {
    		for(int i=0; i<listeners.size(); i++)
    			listeners.elementAt(i).demand(selector, Long.parseLong(msgElement.toString()) );
        }
    }
    
    static private void parseSuggest(Message msg, Vector<ITSCallback> listeners) throws MalformedMessageException {
    	final IModel model = parseModel(msg);
		for(int i=0; i<listeners.size(); i++)
			listeners.elementAt(i).suggest(model);
    }
    
    static private void parseResponseDemands(Message msg, Vector<ITSCallback> listeners) { 
		final MessageElement msgElement = msg.getMessageElement(null,Properties.BYTES);
		if ( msgElement!=null ) {
			for(int i=0; i<listeners.size(); i++)
				listeners.elementAt(i).responseDemands(msgElement.getBytes(false));
		}
    }
    
    static public Message createResponseMessage(String peername, String graphuri, IModel triples) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.RESPONSE, null));
		ret.addMessageElement(null, new StringMessageElement(Properties.GRAPHURI, graphuri, null));
		ret = addModel(ret,triples);
    	return ret;
	}
    
    static public Message createResponseMessage(String peername, ITemplate tpl, IModel triples) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.RESPONSE, null));
		ret = addSelector(ret, tpl);
		ret = addModel(ret, triples);
    	return ret;
	}
    
    static public Message createResponseMessage(String peername, ITemplate tpl, String advertiseSubscribeURI) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.RESPONSE, null));
		ret = addSelector(ret, tpl);
		ret.addMessageElement(null, new StringMessageElement(Properties.TEMPLATEURI, advertiseSubscribeURI, null));
    	return ret;
	}
    
    static public Message createQueryMessage(String peername, ITemplate template) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.QUERY, null));
		ret = addSelector(ret, template);
    	return ret;
	}
	
    static public Message createQueryMultipleMessage(String peername, ITemplate[] selectors) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.QUERYMULTIPLE, null));
		String tplsMsg = ""; 
		for(int i=0; i<selectors.length; i++) {
			ITemplate selector = selectors[i];
			tplsMsg += selector.toString()+"\n";
		}
		ret.addMessageElement(null, new StringMessageElement(Properties.TEMPLATE_MULTIPLE, tplsMsg, null));
		return ret;
	}
	
    static public Message createReadMessage(String peername, ITemplate template) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.READ, null));
		ret = addSelector(ret, template);
    	return ret;
	}
    
    static public Message createTakeMessage(String peername, ITemplate template) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.TAKE, null));
		ret = addSelector(ret, template);
    	return ret;
	}
    
    static public Message createReadMessage(String peername, String graphuri) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.READ, null));
		ret.addMessageElement(null, new StringMessageElement(Properties.GRAPHURI, graphuri, null));
    	return ret;
	}
    
    static public Message createTakeMessage(String peername, String graphuri) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.TAKE, null));
		ret.addMessageElement(null, new StringMessageElement(Properties.GRAPHURI, graphuri, null));
    	return ret;
	}
    
    static public Message createNotifyMessage(String peername, ITemplate template) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.NOTIFY, null));
		ret = addSelector(ret, template);
    	return ret;
	}
    
    //it is a non-sense to call to this method in  the ProxyME, except for testing purposes
    static public Message createAdvertiseMessage(String peername, ITemplate template) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.ADVERTISE, null));
		ret = addSelector(ret, template);
    	return ret;
	}
    
	//not sure if it has sense to call to this method in  the ProxyME, except for testing purposes
    static public Message createUnadvertiseMessage(String peername, String adverturi) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.UNADVERTISE, null));
		ret.addMessageElement(null, new StringMessageElement(Properties.TEMPLATEURI, adverturi, null));
    	return ret;
	}
    
    //it is a non-sense to call to this method in  the ProxyME, except for testing purposes
    static public Message createSubscribeMessage(String peername, String subscribeuri) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.SUBSCRIBE, null));
		ret.addMessageElement(null, new StringMessageElement(Properties.TEMPLATEURI, subscribeuri, null));
    	return ret;
	}
    
    //it is a non-sense to call to this method in  the ProxyME, except for testing purposes
    static public Message createSubscribeMessage(String peername, ITemplate template) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.SUBSCRIBE, null));
		ret = addSelector(ret, template);
    	return ret;
	}
    
	//not sure if it has sense to call to this method in  the ProxyME, except for testing purposes
    static public Message createUnsubscribeMessage(String peername, String subscriptionuri) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.UNSUBSCRIBE, null));
		ret.addMessageElement(null, new StringMessageElement(Properties.TEMPLATEURI, subscriptionuri, null));
    	return ret;
	}
    
    static public Message createDemandMessage(String peername, ITemplate template, long leaseTime) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.DEMAND, null));
		ret = addSelector(ret, template);
		ret.addMessageElement(null, new StringMessageElement(Properties.LEASE_TIME, String.valueOf(leaseTime), null));
    	return ret;
	}
    
    static public Message createSuggestMessage(String peername, IModel triples) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.SUGGEST, null));
		ret = addModel(ret, triples);
    	return ret;
	}
    
    static public Message createObtainDemandsMessage(String peername) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.OBTAIN_DMNDS, null));
    	return ret;
	}
    
    
    static public Message createResponseDemandsMessage(String peername, byte[] bytes) {
		Message ret = createHeader(peername);
		ret.addMessageElement(null, new StringMessageElement(Properties.REQUESTYPE, TypeRequest.RESPONSE_DMNDS, null));
		ret.addMessageElement(null, new ByteArrayMessageElement(Properties.BYTES, MimeMediaType.TEXT_DEFAULTENCODING, bytes, null));
    	return ret;
	}
    
	    static private Message createHeader(String peername) {
	    	Message ret = new Message();
	    	ret.addMessageElement(null, new StringMessageElement(Properties.SENDER, ((peername!=null)?peername:"default"), null) );
	    	return ret;
	    }
	    
		static private Message addSelector(Message msg, ITemplate template) {
			msg.addMessageElement(null, new StringMessageElement(Properties.TEMPLATE, template.toString(), null));
			return msg;
		}
		
		static private Message addModel(Message msg, IModel triples) {
	        //System.err.println("Modelo a enviar");
	        //System.err.println("---------------");
	        //triples.write(System.out, "N-TRIPLE");
	        final Graph graph = triples.write(SemanticFormat.NTRIPLES);
	        msg.addMessageElement(null, new ByteArrayMessageElement(Properties.MODEL, MimeMediaType.TEXT_DEFAULTENCODING, graph.getData().getBytes(), null));
			return msg;
		}
}