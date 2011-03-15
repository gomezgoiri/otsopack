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

import otsopack.commons.converters.impl.DefaultNTriplesUnionUtility;
import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.impl.SemanticFormatsManager;

// TODO: change this name
public class UnionUtility {

	private static final Vector unionUtilities = new Vector();
	private static final SemanticFormatsManager formatsManager = new SemanticFormatsManager();
	
	static{
		addDefaultUnionUtilities();
	}
	
	private static void addDefaultUnionUtilities(){
		addUnionUtility(new DefaultNTriplesUnionUtility());
	}
	
	public static void addUnionUtility(IUnionUtility utility){
		unionUtilities.addElement(utility);
	}
	
	public static void reset(){
		unionUtilities.removeAllElements();
		addDefaultUnionUtilities();
	}
	
	private static boolean canConvertOutputGraph(IUnionUtility utility, SemanticFormat outputFormat){
		if(utility.isOutputSupported(outputFormat))
			return true;
		
		final SemanticFormat [] supportedOutputFormats = utility.getSupportedOutputFormats();
		for(int i = 0; i < supportedOutputFormats.length; ++i)
			if(formatsManager.canConvert(outputFormat, supportedOutputFormats[i]))
				return true;
		
		return false;
	}
	
	private static Graph convertToValidInputGraph(IUnionUtility unionUtility, Graph graph){
		if(unionUtility.isInputSupported(graph.getFormat()))
			return graph;
		
		final SemanticFormat [] supportedInputFormats = unionUtility.getSupportedInputFormats();
		for(int i = 0; i < supportedInputFormats.length; ++i)
			if(formatsManager.canConvert(graph.getFormat(), supportedInputFormats[i]))
				return formatsManager.convert(graph, supportedInputFormats[i]);
		
		return null;
	}
	
	private static boolean canConvertToValidInputGraph(IUnionUtility unionUtiliy, Graph graph){
		if(unionUtiliy.isInputSupported(graph.getFormat()))
			return true;
		
		final SemanticFormat [] supportedInputFormats = unionUtiliy.getSupportedInputFormats();
		for(int i = 0; i < supportedInputFormats.length; ++i)
			if(formatsManager.canConvert(graph.getFormat(), supportedInputFormats[i]))
				return true;
		
		return false;
	}
	
	public static Graph union(Graph graph1, Graph graph2, SemanticFormat outputFormat){
		for(int i = 0; i < unionUtilities.size(); ++i){
			final IUnionUtility unionUtility = (IUnionUtility)unionUtilities.elementAt(i);
			
			if(canConvertToValidInputGraph(unionUtility, graph1) 
					&& canConvertToValidInputGraph(unionUtility, graph2)
					&& canConvertOutputGraph(unionUtility, outputFormat)){
				final Graph convertedGraph1 = convertToValidInputGraph(unionUtility, graph1);
				final Graph convertedGraph2 = convertToValidInputGraph(unionUtility, graph2);
				final Graph resultingGraph = unionUtility.union(convertedGraph1, convertedGraph2);
				return formatsManager.convert(resultingGraph, outputFormat);
			}
		}
		
		throw new IllegalArgumentException("Can't convert other formats that ntriples at the moment ");
	}
}
