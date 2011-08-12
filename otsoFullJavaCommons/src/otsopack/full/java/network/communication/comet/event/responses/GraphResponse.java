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
package otsopack.full.java.network.communication.comet.event.responses;

import java.io.Serializable;

import otsopack.commons.authz.entities.EntityDecodingException;
import otsopack.commons.authz.entities.EntityFactory;
import otsopack.commons.authz.entities.IEntity;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.SignedGraph;
import otsopack.full.java.network.communication.comet.CometException;

public class GraphResponse implements Serializable {
	
	private static final long serialVersionUID = 5229230507440374459L;
	
	private String data;
	private String semanticFormat;
	private String serializedEntity; // If it is a SignedGraph, otherwise null

	public GraphResponse() {}

	public GraphResponse(String data, String semanticFormat, String serializedEntity) {
		this.data = data;
		this.semanticFormat = semanticFormat;
		this.serializedEntity = serializedEntity;
	}
	
	public static GraphResponse create(Graph graph){
		if(graph == null)
			return null;
		if(graph instanceof SignedGraph){
			final SignedGraph signedGraph = (SignedGraph)graph;
			final String serializedEntity = signedGraph.getEntity().serialize();
			return new GraphResponse(graph.getData(), graph.getFormat().getName(), serializedEntity);
		}
		return new GraphResponse(graph.getData(), graph.getFormat().getName(), null);
	}
	
	public Graph toGraph() throws CometException {
		if(this.serializedEntity == null)
			return new Graph(this.data, SemanticFormat.getSemanticFormat(this.semanticFormat));
		
		try{
			final IEntity entity = EntityFactory.create(this.serializedEntity);
			return new SignedGraph(this.data, SemanticFormat.getSemanticFormat(this.semanticFormat), entity);
		}catch(EntityDecodingException e){
			throw new CometException("Could ont deserialize entity: " + e.getMessage(), e);
		}
	}
	
	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSemanticFormat() {
		return this.semanticFormat;
	}

	public void setSemanticFormat(String semanticFormat) {
		this.semanticFormat = semanticFormat;
	}

	public String getSerializedEntity() {
		return this.serializedEntity;
	}

	public void setSerializedEntity(String serializedEntity) {
		this.serializedEntity = serializedEntity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.data == null) ? 0 : this.data.hashCode());
		result = prime
				* result
				+ ((this.semanticFormat == null) ? 0 : this.semanticFormat
						.hashCode());
		result = prime
				* result
				+ ((this.serializedEntity == null) ? 0 : this.serializedEntity
						.hashCode());
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
		GraphResponse other = (GraphResponse) obj;
		if (this.data == null) {
			if (other.data != null)
				return false;
		} else if (!this.data.equals(other.data))
			return false;
		if (this.semanticFormat == null) {
			if (other.semanticFormat != null)
				return false;
		} else if (!this.semanticFormat.equals(other.semanticFormat))
			return false;
		if (this.serializedEntity == null) {
			if (other.serializedEntity != null)
				return false;
		} else if (!this.serializedEntity.equals(other.serializedEntity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GraphResponse [data=" + this.data + ", semanticFormat="
				+ this.semanticFormat + ", serializedEntity="
				+ this.serializedEntity + "]";
	}
}
