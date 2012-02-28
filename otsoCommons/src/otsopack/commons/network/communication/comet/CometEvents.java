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

public class CometEvents {
	
	public static final String READ                   = "read";
	public static final String READ_URI               = READ + "-uri"; 
	public static final String READ_URI_FILTERS       = READ + "-uri-filters"; 
	public static final String READ_TEMPLATE          = READ + "-template";
	public static final String READ_TEMPLATE_FILTERS  = READ + "-template-filters";
	
	public static final String TAKE                   = "take";
	public static final String TAKE_URI               = TAKE + "-uri"; 
	public static final String TAKE_URI_FILTERS       = TAKE + "-uri-filters"; 
	public static final String TAKE_TEMPLATE          = TAKE + "-template";
	public static final String TAKE_TEMPLATE_FILTERS  = TAKE + "-template-filters";
	
	public static final String QUERY                  = "query";
	public static final String QUERY_TEMPLATE         = QUERY + "-template";
	public static final String QUERY_TEMPLATE_FILTERS = QUERY + "-template-filters";
}
