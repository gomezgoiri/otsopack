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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.network.communication.comet;

import otsopack.commons.authz.Filter;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.SerializableTemplate;
import otsopack.commons.data.Template;
import otsopack.commons.data.TemplateSerializingException;
import otsopack.commons.exceptions.AuthorizationException;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.UnsupportedSemanticFormatException;
import otsopack.commons.exceptions.UnsupportedTemplateException;
import otsopack.commons.network.ICommunication;
import otsopack.commons.network.communication.comet.event.Event;
import otsopack.commons.network.communication.comet.event.requests.GraphRequest;
import otsopack.commons.network.communication.comet.event.requests.QueryRequest;
import otsopack.commons.network.communication.comet.event.requests.QueryWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadTemplateRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadTemplateWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadUriRequest;
import otsopack.commons.network.communication.comet.event.requests.ReadUriWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeTemplateRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeTemplateWithFiltersRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeUriRequest;
import otsopack.commons.network.communication.comet.event.requests.TakeUriWithFiltersRequest;
import otsopack.commons.network.communication.event.listener.INotificationListener;

public class Communication2event implements ICommunication {

	@Override
	public void startup() throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() throws TSException {
		// TODO Auto-generated method stub

	}

	@Override
	public Graph read(String spaceURI, String graphURI,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		
		final GraphRequest request = new ReadUriWithFiltersRequest(timeout, outputFormat, graphURI, filters);
		Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_URI_FILTERS, request, spaceURI);
		
		return null;
	}

	@Override
	public Graph read(String spaceURI, String graphURI,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		
		final GraphRequest request = new ReadUriRequest(timeout, outputFormat, graphURI);
		Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_URI, request, spaceURI);
		
		return null;
	}

	@Override
	public Graph read(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {

		if(template instanceof SerializableTemplate){
			final SerializableTemplate serializableTemplate = (SerializableTemplate)template;
			final String serializedTemplate;
			try{
				serializedTemplate = serializableTemplate.serialize();
			}catch(TemplateSerializingException tse){
				tse.printStackTrace();
				return null;
			}
				
			final GraphRequest request = new ReadTemplateWithFiltersRequest(timeout, outputFormat, serializedTemplate, filters);
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_TEMPLATE_FILTERS, request, spaceURI);
		}
		
		return null;
	}

	@Override
	public Graph read(String spaceURI, Template template,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		
		if(template instanceof SerializableTemplate){
			final SerializableTemplate serializableTemplate = (SerializableTemplate)template;
			final String serializedTemplate;
			try{
				serializedTemplate = serializableTemplate.serialize();
			}catch(TemplateSerializingException tse){
				tse.printStackTrace();
				return null;
			}
				
			final GraphRequest request = new ReadTemplateRequest(timeout, outputFormat, serializedTemplate);
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_TEMPLATE, request, spaceURI);
		}
		
		
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		
		final GraphRequest request = new TakeUriWithFiltersRequest(timeout, outputFormat, graphURI, filters);
		Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_URI_FILTERS, request, spaceURI);
		
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		
		final GraphRequest request = new TakeUriRequest(timeout, outputFormat, graphURI);
		Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_URI_FILTERS, request, spaceURI);
		
		return null;
	}
	
	@Override
	public Graph take(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		
		if(template instanceof SerializableTemplate){
			final SerializableTemplate serializableTemplate = (SerializableTemplate)template;
			final String serializedTemplate;
			try{
				serializedTemplate = serializableTemplate.serialize();
			}catch(TemplateSerializingException tse){
				tse.printStackTrace();
				return null;
			}
				
			final GraphRequest request = new TakeTemplateWithFiltersRequest(timeout, outputFormat, serializedTemplate, filters);
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.TAKE_TEMPLATE_FILTERS, request, spaceURI);
		}
		
		return null;
	}

	@Override
	public Graph take(String spaceURI, Template template,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		
		if(template instanceof SerializableTemplate){
			final SerializableTemplate serializableTemplate = (SerializableTemplate)template;
			final String serializedTemplate;
			try{
				serializedTemplate = serializableTemplate.serialize();
			}catch(TemplateSerializingException tse){
				tse.printStackTrace();
				return null;
			}
				
			final GraphRequest request = new TakeTemplateRequest(timeout, outputFormat, serializedTemplate);
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.TAKE_TEMPLATE, request, spaceURI);
		}
		
		return null;
	}

	@Override
	public Graph[] query(String spaceURI, Template template,
			SemanticFormat outputFormat, Filter[] filters, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		
		if(template instanceof SerializableTemplate){
			final SerializableTemplate serializableTemplate = (SerializableTemplate)template;
			final String serializedTemplate;
			try{
				serializedTemplate = serializableTemplate.serialize();
			}catch(TemplateSerializingException tse){
				tse.printStackTrace();
				return null;
			}
				
			final GraphRequest request = new QueryWithFiltersRequest(timeout, outputFormat, serializedTemplate, filters);
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.QUERY_TEMPLATE_FILTERS, request, spaceURI);
		}
		
		return null;
	}

	@Override
	public Graph[] query(String spaceURI, Template template,
			SemanticFormat outputFormat, long timeout)
			throws SpaceNotExistsException, UnsupportedTemplateException,
			UnsupportedSemanticFormatException {
		
		if(template instanceof SerializableTemplate){
			final SerializableTemplate serializableTemplate = (SerializableTemplate)template;
			final String serializedTemplate;
			try{
				serializedTemplate = serializableTemplate.serialize();
			}catch(TemplateSerializingException tse){
				tse.printStackTrace();
				return null;
			}
				
			final GraphRequest request = new QueryRequest(timeout, outputFormat, serializedTemplate);
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.QUERY_TEMPLATE, request, spaceURI);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.ICommunication#subscribe(java.lang.String, otsopack.commons.data.NotificableTemplate, otsopack.commons.network.communication.event.listener.INotificationListener)
	 */
	@Override
	public String subscribe(String spaceURI, NotificableTemplate template,
			INotificationListener listener) throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.ICommunication#unsubscribe(java.lang.String, java.lang.String)
	 */
	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.ICommunication#advertise(java.lang.String, otsopack.commons.data.NotificableTemplate)
	 */
	@Override
	public String advertise(String spaceURI, NotificableTemplate template)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see otsopack.commons.network.ICommunication#unadvertise(java.lang.String, java.lang.String)
	 */
	@Override
	public void unadvertise(String spaceURI, String advertisementURI)
			throws SpaceNotExistsException {
		// TODO Auto-generated method stub

	}
}