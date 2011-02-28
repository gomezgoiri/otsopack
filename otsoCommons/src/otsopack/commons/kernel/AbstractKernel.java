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

package otsopack.commons.kernel;

import otsopack.commons.IController;
import otsopack.commons.ITripleSpace;
import otsopack.commons.data.IGraph;
import otsopack.commons.data.ITemplate;
import otsopack.commons.data.ITriple;
import otsopack.commons.data.impl.SemanticFactory;
import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.INetwork;
import otsopack.commons.network.communication.demand.local.ISuggestionCallback;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.stats.Statistics;
import otsopack.commons.util.Util;

/**
 * triple space implemetation | main class
 * @author Aitor Gómez Goiri
 */
public abstract class AbstractKernel implements ITripleSpace {
	private IController controller = null;
	private IDataAccess dataAccessService = null;
	protected INetwork networkService = null;
	
	private boolean connected = false;

	
	/**
	 * Prepares the Kernel to startup.
	 * 
	 * This was previously done in the constructor, moved to ease the tests.
	 * @throws TSException 
	 * @throws TSException 
	 * @throws TSException 
	 */
	protected void buildKernel() {
		if (this.getController() == null) {
			this.setController(new Controller(this));
		}
		
		if (this.dataAccessService == null) {
			this.setDataAccessService(new MemoryDataAccess());
		}
	}
	
//	/**
//	 * constructor
//	 * creates the DataAccessService according to general.properties
//	 * the NetworkService
//	 * optionally the MetaDataManager
//	 */
//	public Kernel() {
//		controller = new Controller(this);
//		dataAccessService = new StoreManager();
//		if( Props.NETWORK_LAYER == Props.PROXYLESS ) {
//			try {
//				networkService = new es.deusto.tecnologico.tscME.network.jxme.proxyless.JxmeNetwork(controller);
//			} catch (TSException e) {
//				Printer.err_println(e.getMessage());
//			}
//		} else networkService = new es.deusto.tecnologico.tscME.network.jxme.withproxy.JxmeNetwork(controller);
//	}
	
	public void startup() throws TSException {
		this.buildKernel();

		this.dataAccessService.startup();		
		this.networkService.startup();
		
		//if no exception the Kernel is connected
		this.connected = true;
	}
	
	public void shutdown() throws TSException {
		if (this.dataAccessService != null)
			getDataAccessService().shutdown();
		
		if (this.networkService != null)
			getNetworkService().shutdown();
		
		this.setController(null);
		this.dataAccessService = null;
		this.networkService = null;
		
		this.connected = false;
	}	

	public void createSpace(String spaceURI) throws TSException {
		if (spaceURI != null) {
			spaceURI = Util.normalizeSpaceURI(spaceURI, "");
			networkService.createSpace(spaceURI);
			dataAccessService.createSpace(spaceURI);			
		} else {
			throw new TSException("Space must not be null");
		}	
	}

	public void joinSpace(String spaceURI) throws TSException {
		if (spaceURI != null) {
			spaceURI = Util.normalizeSpaceURI(spaceURI, "");
			networkService.joinSpace(spaceURI);
			dataAccessService.joinSpace(spaceURI);			
		} else {
			throw new TSException("space must not be null");
		}		
	}

	public void leaveSpace(String spaceURI) throws TSException {
		if (spaceURI != null) {
			spaceURI = Util.normalizeSpaceURI(spaceURI, "");
			if(networkService.getJoinedSpaces().contains(spaceURI)) {
				
				/*boolean sendTriplesOnLeave = true;
				try {
					if (Props.Jxta.SEND_TRIPLES_ON_LEAVE.getProperty().toLowerCase().equals("true")
							|| Props.Jxta.SEND_TRIPLES_ON_LEAVE.getProperty().toLowerCase().equals(
									"false")) {
						sendTriplesOnLeave = new Boolean(Props.Jxta.SEND_TRIPLES_ON_LEAVE.getProperty());
					} else {
						throw new PropertiesException("jxta.properties SEND_TRIPLES_ON_LEAVE not true or false using standard value = true");
					}
				} catch (PropertiesException e) {
					e.printStackTrace();
				}
				
				if(sendTriplesOnLeave) {
					Set<URI> graphs = dataAccessService.getGraphs(spaceURI);
					
					Set<ITriple> triples = new HashSet<ITriple>();
					for (Iterator<URI> iterator = graphs.iterator(); iterator.hasNext();) {
						triples.addAll(
								dataAccessService.read(spaceURI, (URI) iterator.next()));
					}
					
					if(triples.size() > 0) {
						try {
							//System.out.println("sending " + triples.size() + " triples to other kernel");
							networkService.write(spaceURI, triples, null);
						} catch (SpaceNotExistsException e) {
							e.printStackTrace();
						}
					}
					
				}*/
				
				networkService.leaveSpace(spaceURI);
				dataAccessService.leaveSpace(spaceURI);
			}
		} else throw new SpaceNotExistsException("space must not be null");
	}
	
	public void unsubscribe(String spaceURI, String subscriptionURI) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		networkService.unsubscribe(spaceURI, subscriptionURI);
	}	

	public String subscribe(String spaceURI, ITemplate template, INotificationListener listener) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		return networkService.subscribe(spaceURI, template, listener);
	}
	
	public String advertise(String spaceURI, ITemplate template) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		return networkService.advertise(spaceURI, template);
	}
	
	public void unadvertise(String spaceURI, String advertisementURI) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		networkService.unadvertise(spaceURI, advertisementURI);
	}

	public IGraph query(String spaceURI, ITemplate template, long timeout) {
		IGraph ret = null;
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		try {
			IGraph localmodel = dataAccessService.query(spaceURI, template); 
				if(localmodel!=null) ret = localmodel;
			IGraph netmodel = networkService.query(spaceURI, template, timeout);
				if(netmodel!=null) {
					if(ret==null) ret = netmodel;
					else ret.addAll(netmodel);
				}
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public IGraph read(String spaceURI, ITemplate template, long timeout) {
		IGraph ret = null;
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		try {
			ret = networkService.read(spaceURI, template, timeout);
			if(ret==null) ret = dataAccessService.read(spaceURI, template);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public IGraph read(String spaceURI, String graphURI, long timeout) {
		IGraph ret = null;
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		//graphURI = Util.normalizeSpaceURI(graphURI, "");
		try {
			ret = networkService.read(spaceURI, graphURI, timeout); 
			if(ret==null) ret = dataAccessService.read(spaceURI, graphURI);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public IGraph take(String spaceURI, ITemplate template, long timeout) {
		IGraph ret = null;
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		try {
			ret = networkService.take(spaceURI, template, timeout); 
			if(ret==null) ret = dataAccessService.take(spaceURI, template);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public IGraph take(String spaceURI, String graphURI, long timeout) {
		IGraph ret = null;
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		//graphURI = Util.normalizeSpaceURI(graphURI, "");
		try {
			ret = networkService.take(spaceURI, graphURI, timeout);
			if(ret==null) ret = dataAccessService.take(spaceURI, graphURI);
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public String write(String spaceURI, IGraph triples) throws TSException {
		//TODO ### db:24002008 writing to a space without joining it? data is now there - locally - but no one can find it, temporary join space and write data?! therefore new write method in networkService...
		final long start = System.currentTimeMillis();
		if( spaceURI!=null && triples!=null && triples.size()>=0 ) {
			final String ret;
			spaceURI = Util.normalizeSpaceURI(spaceURI, "");
			if( !networkService.getJoinedSpaces().contains(spaceURI) ) {
				new TSException("space " + spaceURI + " must be joined before write can be performed").printStackTrace();
			}
			
			//new implementation of write primitive
			if( networkService.callbackIfIHaveResponsabilityOverThisKnowlege(spaceURI, triples) ) {
				ret = null;
			} else if( networkService.hasAnyPeerResponsabilityOverThisKnowlege(spaceURI, triples) ) {
				// should be call it even if local callbacks have been performed?
				networkService.suggest(spaceURI, triples);
				ret = null;
			} else {
				/*URI graphURI = */
				ret = dataAccessService.write(spaceURI, triples);
			}
			final long timeneeded = System.currentTimeMillis() - start;
			Statistics.addMeasure("write", timeneeded, System.currentTimeMillis());
			return ret;
		}
		throw new TSException("space uri and triples must not be null");
	}
	
	public String write(String spaceURI, ITriple[] triples) throws TSException {
		final SemanticFactory sf = new SemanticFactory();
		IGraph trips = sf.createEmptyGraph();
		for(int i=0; i<triples.length; i++) {
			trips.add(triples[i]);
		}
		return write(spaceURI, trips);
	}

	public String write(String spaceURI, ITriple triple) throws TSException {
		ITriple[] triples = new ITriple[1];
		triples[0] = triple;
		return write(spaceURI, triples);
	}
	
	public IDataAccess getDataAccessService() {
		return dataAccessService;
	}
	
	public INetwork getNetworkService() {
		return networkService;
	}
	public void setDataAccessService(IDataAccess dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	public void setNetworkService(INetwork networkService) {
		this.networkService = networkService;
	}

	public boolean isConnected() {
		return this.connected ;
	}

	private void setController(IController controller) {
		this.controller = controller;
	}

	protected IController getController() {
		return controller;
	}

	public void demand(String spaceURI, ITemplate template, long leaseTime,
			ISuggestionCallback callback) throws TSException {
		networkService.demand(spaceURI, template, leaseTime, callback);
	}	
}