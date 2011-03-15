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

public class Graph {
	private final String data;
	private final SemanticFormat format;
	
	public Graph(String data, SemanticFormat format){
		this.data   = data;
		this.format = format;
	}
	
	public String getData(){
		return this.data;
	}
	
	public SemanticFormat getFormat(){
		return this.format;
	}
}
