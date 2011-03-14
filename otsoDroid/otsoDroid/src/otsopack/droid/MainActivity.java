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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */
package otsopack.droid;

import java.net.URI;
import java.net.URISyntaxException;

import otsopack.commons.data.Graph;
import otsopack.commons.data.SemanticFormats;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.data.impl.microjena.MicrojenaFactory;
import otsopack.commons.data.impl.microjena.ModelImpl;
import otsopack.commons.exceptions.SpaceAlreadyExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.exceptions.TripleParseException;
import otsopack.droid.configuration.JxmeConfiguration;
import otsopack.droid.kernel.Kernel;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Thread t = new Thread(){
			@Override
			public void run(){
				initialize();
				startup();
			}
		};
		t.start();
		setContentView(R.layout.main);
	}
	
	public static final  String space = "http://www.morelab.deusto.es/scenario/havoc";
	Kernel kernel;
	MicrojenaFactory microjenaFactory;
	SemanticFactory factory;
	
	private void initialize(){
		kernel = new Kernel();
		microjenaFactory = new MicrojenaFactory();
		SemanticFactory.initialize(microjenaFactory);
		factory = new SemanticFactory();
		Log.e("MainActivity", "SemanticFactory created");
	}
	
	private void configureJxme() {
		JxmeConfiguration jxmeConfiguration = JxmeConfiguration.getConfiguration();
		
		jxmeConfiguration.setPeerName("mymobile");
		
		jxmeConfiguration.setUseDefaultSeeds(false);
		jxmeConfiguration.setRendezvousName("IsmedRendezvous");
		try {
			jxmeConfiguration.setRendezvousURI(new URI("tcp://192.168.59.6:9701"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		jxmeConfiguration.setTcpPort(48000);
		jxmeConfiguration.setTcpStartPort(48101);
		jxmeConfiguration.setTcpEndPort(48200);
		jxmeConfiguration.setRendezvousConnectionTimeout(0);
	}
	
	private void startup(){
		configureJxme();
		try {
			kernel.startup();
			try{
				kernel.createSpace(space);
			} catch (SpaceAlreadyExistsException e) {
				e.printStackTrace();
			}
			kernel.joinSpace(space);
		} catch (TSException e) {
			e.printStackTrace();
		}
		try {
			ModelImpl model = new ModelImpl();
			model.addTriple("http://www.morelab.deusto.es/sub", "http://www.morelab.deusto.es/pred", "http://www.morelab.deusto.es/obj");
			kernel.write(space, model.write(SemanticFormats.NTRIPLES));
			Graph graph = kernel.query(space, factory.createTemplate("?s ?p ?o ."), SemanticFormats.NTRIPLES, 5000);
			//final Enumeration<?> enume = graph.elements();
			//while(enume.hasMoreElements())
				//System.out.println(enume.nextElement());
			System.out.println("Gathered: " + graph.getFormat());
			System.out.println(graph.getData());
		} catch (TripleParseException e) {
			e.printStackTrace();
		} catch (TSException e) {
			e.printStackTrace();
		}
	}
}
