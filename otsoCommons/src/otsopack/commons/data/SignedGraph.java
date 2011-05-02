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
package otsopack.commons.data;

import otsopack.commons.authz.entities.IEntity;

public class SignedGraph extends Graph {

	private final IEntity entity;
	
	/**
	 * @param data
	 * @param format
	 */
	public SignedGraph(String data, SemanticFormat format, IEntity entity) {
		super(data, format);
		this.entity = entity;
	}

	public IEntity getEntity(){
		return this.entity;
	}
}
