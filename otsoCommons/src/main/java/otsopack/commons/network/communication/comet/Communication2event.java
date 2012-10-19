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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.network.communication.comet;

import otsopack.commons.Arguments;
import otsopack.commons.data.Graph;
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
	public Graph read(String spaceURI, String graphURI, Arguments configuration)
			throws SpaceNotExistsException, AuthorizationException, UnsupportedSemanticFormatException {
		if (configuration.getFilters()!=null && !configuration.getFilters().isEmpty()) {
			final GraphRequest request = new ReadUriWithFiltersRequest(configuration.getTimeout(), configuration.getOutputFormat(), graphURI, configuration.getFilters());
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_URI_FILTERS, request, spaceURI);
		} else {
			final GraphRequest request = new ReadUriRequest(configuration.getTimeout(), configuration.getOutputFormat(), graphURI);
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_URI, request, spaceURI);
		}
		
		return null;
	}

	@Override
	public Graph read(String spaceURI, Template template, Arguments configuration)
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
			
			if (configuration.getFilters()!=null && !configuration.getFilters().isEmpty()) {
				final GraphRequest request = new ReadTemplateWithFiltersRequest(configuration.getTimeout(), configuration.getOutputFormat(), serializedTemplate, configuration.getFilters());
				Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_TEMPLATE_FILTERS, request, spaceURI);
			} else {
				final GraphRequest request = new ReadTemplateRequest(configuration.getTimeout(), configuration.getOutputFormat(), serializedTemplate);
				Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_TEMPLATE, request, spaceURI);
			}
		}
		
		return null;
	}

	@Override
	public Graph take(String spaceURI, String graphURI, Arguments configuration)
			throws SpaceNotExistsException, AuthorizationException,
			UnsupportedSemanticFormatException {
		if (configuration.getFilters()!=null && !configuration.getFilters().isEmpty()) {
			final GraphRequest request = new TakeUriWithFiltersRequest(configuration.getTimeout(), configuration.getOutputFormat(), graphURI, configuration.getFilters());
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_URI_FILTERS, request, spaceURI);
		} else {
			final GraphRequest request = new TakeUriRequest(configuration.getTimeout(), configuration.getOutputFormat(), graphURI);
			Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.READ_URI, request, spaceURI);
		}
		return null;
	}
	
	@Override
	public Graph take(String spaceURI, Template template, Arguments configuration)
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
			
			if (configuration.getFilters()!=null && !configuration.getFilters().isEmpty()) {
				final GraphRequest request = new TakeTemplateWithFiltersRequest(configuration.getTimeout(), configuration.getOutputFormat(), serializedTemplate, configuration.getFilters());
				Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.TAKE_TEMPLATE_FILTERS, request, spaceURI);
			} else {
				final GraphRequest request = new TakeTemplateRequest(configuration.getTimeout(), configuration.getOutputFormat(), serializedTemplate);
				Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.TAKE_TEMPLATE, request, spaceURI);
			}
		}
		
		return null;
	}

	@Override
	public Graph[] query(String spaceURI, Template template, Arguments configuration)
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
			
			if (configuration.getFilters()!=null && !configuration.getFilters().isEmpty()) {
				final GraphRequest request = new QueryWithFiltersRequest(configuration.getTimeout(), configuration.getOutputFormat(), serializedTemplate, configuration.getFilters());
				Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.QUERY_TEMPLATE_FILTERS, request, spaceURI);
			} else {
				final GraphRequest request = new QueryRequest(configuration.getTimeout(), configuration.getOutputFormat(), serializedTemplate);
				Event event = new Event(Event.TYPE_REQUEST, Event.generateEventId(), CometEvents.QUERY_TEMPLATE, request, spaceURI);
			}
		}
		
		return null;
	}
}