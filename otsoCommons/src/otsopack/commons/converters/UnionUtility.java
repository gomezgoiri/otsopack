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
package otsopack.commons.converters;

import java.util.Vector;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormats;
import otsopack.commons.data.impl.SemanticFormatsManager;

// TODO: change this name
public class UnionUtility {

	private static final Vector unionUtilities = new Vector();
	private static final SemanticFormatsManager formatsManager = new SemanticFormatsManager();
	
	public static void addUnionUtility(IUnionUtility utility){
		unionUtilities.add(utility);
	}
	
	public static void reset(){
		unionUtilities.removeAllElements();
	}
	
	private static Graph convertToValidInputGraph(IUnionUtility utility, Graph graph){
		if(utility.isInputSupported(graph.getFormat()))
			return graph;
		
		final String [] supportedInputFormats = utility.getSupportedInputFormats();
		for(int i = 0; i < supportedInputFormats.length; ++i)
			if(formatsManager.canConvert(graph.getFormat(), supportedInputFormats[i]))
				return formatsManager.convert(graph, supportedInputFormats[i]);
		
		return null;
	}
	
	private static boolean canConvertToValidInputGraph(IUnionUtility utility, Graph graph){
		if(utility.isInputSupported(graph.getFormat()))
			return true;
		
		final String [] supportedInputFormats = utility.getSupportedInputFormats();
		for(int i = 0; i < supportedInputFormats.length; ++i)
			if(formatsManager.canConvert(graph.getFormat(), supportedInputFormats[i]))
				return true;
		
		return false;
	}
	
	public static Graph union(Graph graph1, Graph graph2){
		
		for(int i = 0; i < unionUtilities.size(); ++i){
			final IUnionUtility unionUtility = (IUnionUtility)unionUtilities.get(i);
			
			if(canConvertToValidInputGraph(unionUtility, graph1) && canConvertToValidInputGraph(unionUtility, graph2))
				;
			
		}
		
		
		if(graph1.getFormat().equals(SemanticFormats.NTRIPLES) && graph2.getFormat().equals(SemanticFormats.NTRIPLES))
			return new Graph(graph1.getData() + "\n" + graph2.getData(), SemanticFormats.NTRIPLES);
		// TODO
		throw new IllegalArgumentException("Can't convert other formats that ntriples at the moment ");
	}
}
