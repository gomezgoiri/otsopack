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
package otsopack.commons.network.communication.comet.event.requests;

import java.io.Serializable;

import otsopack.commons.Arguments;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.SerializableTemplateFactory;
import otsopack.commons.data.Template;
import otsopack.commons.data.TemplateDeserializingException;
import otsopack.commons.network.communication.util.JSONEncoder;

/**
 * A {@link GraphRequest} collects those requests that are will
 * get a graph or set of graphs as response. They all have a set
 * of common arguments (such as a timeout, output format, etc.)
 */
public abstract class GraphRequest implements Serializable {
	
	private static final long serialVersionUID = -2879892695503325088L;
	
	private long timeout;
	private SemanticFormat outputFormat;

	public GraphRequest(){}
	
	public GraphRequest(long timeout, SemanticFormat outputFormat) {
		this.timeout = timeout;
		this.outputFormat = outputFormat;
	}
	
	public String serialize(){
		return JSONEncoder.encode(this);
	}

	protected Template getTemplate(HasTemplateRequest request) throws TemplateDeserializingException {
		final String serializedTemplate = request.getSerializedTemplate();
		return (Template)SerializableTemplateFactory.create(serializedTemplate);
	}
	
	public Arguments getArguments() {
		return new Arguments().setOutputFormat(getOutputFormat()).setTimeout(getTimeout());
	}
	
	public long getTimeout() {
		return this.timeout;
	}
	
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	public SemanticFormat getOutputFormat() {
		return this.outputFormat;
	}
	
	public void setOutputFormat(SemanticFormat outputFormat) {
		this.outputFormat = outputFormat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.outputFormat == null) ? 0 : this.outputFormat
						.hashCode());
		result = prime * result + (int) (this.timeout ^ (this.timeout >>> 32));
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
		GraphRequest other = (GraphRequest) obj;
		if (this.outputFormat == null) {
			if (other.outputFormat != null)
				return false;
		} else if (!this.outputFormat.equals(other.outputFormat))
			return false;
		if (this.timeout != other.timeout)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GraphRequest [timeout=" + this.timeout + ", outputFormat="
				+ this.outputFormat + "]";
	}
}
