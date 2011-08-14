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
package otsopack.full.java.network.communication.comet.event.requests;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.ICommunication;

public class ReadTemplateRequest extends ReadRequest implements HasTemplateRequest {

	private static final long serialVersionUID = -6769270354929460223L;
	
	private String serializedTemplate;
	
	public ReadTemplateRequest() {
	}

	public ReadTemplateRequest(long timeout, SemanticFormat outputFormat, String serializedTemplate) {
		super(timeout, outputFormat);
		this.serializedTemplate = serializedTemplate;
	}
	
	@Override
	public Graph read(String spaceURI, ICommunication comm) throws TSException {
		return comm.read(spaceURI, getTemplate(this), getOutputFormat(), getTimeout());
	}

	public String getSerializedTemplate() {
		return this.serializedTemplate;
	}

	public void setSerializedTemplate(String serializedTemplate) {
		this.serializedTemplate = serializedTemplate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((this.serializedTemplate == null) ? 0
						: this.serializedTemplate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReadTemplateRequest other = (ReadTemplateRequest) obj;
		if (this.serializedTemplate == null) {
			if (other.serializedTemplate != null)
				return false;
		} else if (!this.serializedTemplate.equals(other.serializedTemplate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReadTemplateRequest [serializedTemplate="
				+ this.serializedTemplate + ", getTimeout()="
				+ this.getTimeout() + ", getOutputFormat()="
				+ this.getOutputFormat() + "]";
	}
}
