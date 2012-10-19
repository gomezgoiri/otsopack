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
package otsopack.commons.network.communication.comet.event;

import java.io.Serializable;
import java.util.UUID;

import otsopack.commons.network.communication.comet.CometEvents;
import otsopack.commons.network.communication.comet.event.requests.GraphRequest;

public class Event implements Serializable {
	
	private static final long serialVersionUID = -8414362780443364402L;

	public static final String TYPE_POLL                = "poll";
	public static final String TYPE_REQUEST             = "request";
	public static final String TYPE_SUCCESSFUL_RESPONSE = "response::success";
	public static final String TYPE_ERROR_RESPONSE      = "response::error";
	
	/**
	 * What kind of event is this one: poll, request, response  
	 */
	private String type;
	
	public static String generateEventId(){
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Each request provides a unique identifier, and the response must carry it
	 */
	private String eventId;
	
	/**
	 * Triple Space operation: read, take, query (notify, etc.). See {@link CometEvents}
	 */
	private String operation;
	
	/**
	 * On which Space it is working.
	 */
	private String spaceUri;
	
	/**
	 * If it is a request, carry the request in a serialized form. 
	 * If it is a response, carry the response in a serialized form.
	 */
	private String payload;

	public Event(){}
	
	public Event(String type, String eventId, String operation, String payload, String spaceUri) {
		this.type = type;
		this.eventId = eventId;
		this.operation = operation;
		this.payload = payload;
		this.spaceUri = spaceUri;
	}

	public Event(String type, String eventId, String operation, GraphRequest request, String spaceUri) {
		this.type = type;
		this.eventId = eventId;
		this.operation = operation;
		this.payload = request.serialize();
		this.spaceUri = spaceUri;
	}
	public boolean isRequest(){
		return this.type.equals("request");
	}

	public boolean isResponse(){
		return this.type.equals("request");
	}
	
	public boolean isPoll(){
		return this.type.equals("poll");
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getPayload() {
		return this.payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public String getSpaceURI() {
		return this.spaceUri;
	}
	
	public void setSpaceURI(String spaceUri) {
		this.spaceUri = spaceUri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.eventId == null) ? 0 : this.eventId.hashCode());
		result = prime * result
				+ ((this.operation == null) ? 0 : this.operation.hashCode());
		result = prime * result
				+ ((this.payload == null) ? 0 : this.payload.hashCode());
		result = prime * result
				+ ((this.spaceUri == null) ? 0 : this.spaceUri.hashCode());
		result = prime * result
				+ ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (this.eventId == null) {
			if (other.eventId != null)
				return false;
		} else if (!this.eventId.equals(other.eventId))
			return false;
		if (this.operation == null) {
			if (other.operation != null)
				return false;
		} else if (!this.operation.equals(other.operation))
			return false;
		if (this.payload == null) {
			if (other.payload != null)
				return false;
		} else if (!this.payload.equals(other.payload))
			return false;
		if (this.spaceUri == null) {
			if (other.spaceUri != null)
				return false;
		} else if (!this.spaceUri.equals(other.spaceUri))
			return false;
		if (this.type == null) {
			if (other.type != null)
				return false;
		} else if (!this.type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [type=" + this.type + ", eventId=" + this.eventId
				+ ", operation=" + this.operation + ", spaceUri="
				+ this.spaceUri + ", payload=" + this.payload + "]";
	}
}
