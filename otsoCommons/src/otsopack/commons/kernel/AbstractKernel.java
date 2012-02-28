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
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.commons.kernel;

import java.util.Set;

import otsopack.commons.IController;
import otsopack.commons.ITripleSpace;
import otsopack.commons.authz.Filter;
import otsopack.commons.converters.UnionManager;
import otsopack.commons.data.Graph;
import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.SemanticFormat;
import otsopack.commons.data.Template;
import otsopack.commons.dataaccess.IDataAccess;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.commons.exceptions.SpaceNotExistsException;
import otsopack.commons.exceptions.TSException;
import otsopack.commons.network.INetwork;
import otsopack.commons.network.communication.event.listener.INotificationListener;
import otsopack.commons.stats.Statistics;
import otsopack.commons.util.Util;

/**
 * triple space implementation | main class
 * @author Aitor Gómez Goiri
 */
public abstract class AbstractKernel implements ITripleSpace {
	private IController controller = null;
	private IDataAccess dataAccessService = null;
	protected INetwork networkService = null;
	
	private boolean connected = false;
	protected int defaultTimeout = 5000;
	protected SemanticFormat defaultSemanticFormat = SemanticFormat.NTRIPLES;

	
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
	
	@Override
	public int getDefaultTimeout(){
		return this.defaultTimeout;
	}
	
	@Override
	public SemanticFormat getDefaultSemanticFormat(){
		return this.defaultSemanticFormat;
	}
	
	@Override
	public void setDefaultTimeout(int timeout){
		this.defaultTimeout = timeout;
	}
	
	@Override
	public void setDefaultSemanticFormat(SemanticFormat format){
		this.defaultSemanticFormat = format;
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
	
	@Override
	public void startup() throws TSException {
		this.buildKernel();

		this.dataAccessService.startup();		
		this.networkService.startup();
		
		//if no exception the Kernel is connected
		this.connected = true;
	}
	
	@Override
	public void shutdown() throws TSException {
		try{
			if (this.dataAccessService != null)
				getDataAccessService().shutdown();
		}finally{
			try{
				if (this.networkService != null)
					getNetworkService().shutdown();
			}finally{
				this.setController(null);
				this.dataAccessService = null;
				this.networkService = null;
				
				this.connected = false;
			}
		}
	}
	
	@Override
	public void createSpace(String spaceURI) throws TSException {
		if (spaceURI != null) {
			spaceURI = Util.normalizeSpaceURI(spaceURI, "");
			networkService.createSpace(spaceURI);
			//dataAccessService.createSpace(spaceURI);			
		} else {
			throw new TSException("Space must not be null");
		}	
	}
	
	@Override
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
	
	@Override
	public Set<String> getSpaces() {
		return this.networkService.getJoinedSpaces();
	}
	
	@Override
	public void unsubscribe(String spaceURI, String subscriptionURI) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		networkService.unsubscribe(spaceURI, subscriptionURI);
	}

	@Override
	public String subscribe(String spaceURI, NotificableTemplate template, INotificationListener listener) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		return networkService.subscribe(spaceURI, template, listener);
	}
	
	@Override
	public String advertise(String spaceURI, NotificableTemplate template) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		return networkService.advertise(spaceURI, template);
	}
	
	@Override
	public void unadvertise(String spaceURI, String advertisementURI) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		networkService.unadvertise(spaceURI, advertisementURI);
	}

	@Override
	public Graph query(String spaceURI, Template template) throws TSException {
		return query(spaceURI, template, this.defaultSemanticFormat, this.defaultTimeout);
	}

	@Override
	public Graph query(String spaceURI, Template template, long timeout) throws TSException {
		return query(spaceURI, template, this.defaultSemanticFormat, timeout);
	}
	
	@Override
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat) throws TSException {
		return query(spaceURI, template, outputFormat, this.defaultTimeout);
	}
	
	@Override
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws TSException {
		return query(spaceURI, template, outputFormat, null, timeout);
	}
	
	@Override
	public Graph query(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException {
		Graph ret = null;
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		try {
			Graph localmodel = dataAccessService.query(spaceURI, template, outputFormat); 
			if( localmodel!=null ) 
				ret = localmodel;
			
			Graph [] netmodel;
			if( filters==null )
				netmodel = networkService.query(spaceURI, template, outputFormat, timeout);
			else
				netmodel = networkService.query(spaceURI, template, outputFormat, filters, timeout);

			if( netmodel != null ) {
				if( ret == null ) 
					ret = UnionManager.union(netmodel, outputFormat); 
				else 
					ret = UnionManager.union(ret, UnionManager.union(netmodel, outputFormat), outputFormat);
			}
		} catch (SpaceNotExistsException e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public Graph read(String spaceURI, Template template) throws TSException {
		return read(spaceURI,template, this.defaultSemanticFormat, this.defaultTimeout);
	}
	
	@Override
	public Graph read(String spaceURI, Template template, long timeout) throws TSException {
		return read(spaceURI,template, this.defaultSemanticFormat, timeout);
	}

	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat) throws TSException {
		return read(spaceURI,template,outputFormat, this.defaultTimeout);
	}
	
	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws TSException {
		return read(spaceURI,template,outputFormat,null,timeout);
	}
	
	@Override
	public Graph read(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		
		final Graph ret = dataAccessService.read(spaceURI, template, outputFormat);
		if(ret != null)
			return ret;
		
		if( filters==null )
			return networkService.read(spaceURI, template, SemanticFormat.NTRIPLES, timeout);
		
		return networkService.read(spaceURI, template, SemanticFormat.NTRIPLES, filters, timeout);
	}

	@Override
	public Graph read(String spaceURI, String graphURI) throws TSException {
		return read(spaceURI,graphURI, this.defaultSemanticFormat);
	}
	
	@Override
	public Graph read(String spaceURI, String graphURI, long timeout) throws TSException {
		return read(spaceURI,graphURI, this.defaultSemanticFormat,timeout);
	}
	
	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat) throws TSException {
		return read(spaceURI,graphURI,outputFormat,this.defaultTimeout);
	}

	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout) throws TSException {
		return read(spaceURI,graphURI,outputFormat,null,timeout);
	}
	
	@Override
	public Graph read(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		//graphURI = Util.normalizeSpaceURI(graphURI, "");
		final Graph ret = dataAccessService.read(spaceURI, graphURI, outputFormat);
		if(ret != null)
			return ret;
		
		if( filters==null )
			return networkService.read(spaceURI, graphURI, SemanticFormat.NTRIPLES, timeout);
		
		return networkService.read(spaceURI, graphURI, SemanticFormat.NTRIPLES, filters, timeout);
	}

	@Override
	public Graph take(String spaceURI, Template template) throws TSException {
		return take(spaceURI,template,this.defaultSemanticFormat);
	}

	@Override
	public Graph take(String spaceURI, Template template, long timeout) throws TSException {
		return take(spaceURI,template,this.defaultSemanticFormat,timeout);
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat) throws TSException {
		return take(spaceURI,template,outputFormat,this.defaultTimeout);
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, long timeout) throws TSException {
		return take(spaceURI,template,outputFormat,null,timeout);
	}

	@Override
	public Graph take(String spaceURI, Template template, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		final Graph ret = dataAccessService.take(spaceURI, template, outputFormat);
		if(ret != null)
			return ret;
		
		if( filters==null )
			return networkService.take(spaceURI, template, SemanticFormat.NTRIPLES, timeout);
		
		return networkService.take(spaceURI, template, SemanticFormat.NTRIPLES, filters, timeout);
	}

	@Override
	public Graph take(String spaceURI, String graphURI) throws TSException {
		return take(spaceURI,graphURI,this.defaultSemanticFormat, this.defaultTimeout);
	}

	@Override
	public Graph take(String spaceURI, String graphURI, long timeout) throws TSException {
		return take(spaceURI,graphURI,this.defaultSemanticFormat,timeout);
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat) throws TSException {
		return take(spaceURI,graphURI,outputFormat,this.defaultTimeout);
	}

	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, long timeout) throws TSException {
		return take(spaceURI,graphURI,outputFormat,null,timeout);
	}
	
	@Override
	public Graph take(String spaceURI, String graphURI, SemanticFormat outputFormat, Filter[] filters, long timeout) throws TSException {
		spaceURI = Util.normalizeSpaceURI(spaceURI, "");
		//graphURI = Util.normalizeSpaceURI(graphURI, "");
		final Graph ret = dataAccessService.take(spaceURI, graphURI, outputFormat);
		
		if(ret != null)
			return ret;
		
		if( filters==null )
			return networkService.take(spaceURI, graphURI, SemanticFormat.NTRIPLES, timeout);
		
		return networkService.take(spaceURI, graphURI, SemanticFormat.NTRIPLES, filters, timeout);
	}

	@Override
	public String write(String spaceURI, Graph triples) throws TSException {
		//TODO ### db:24002008 writing to a space without joining it? data is now there - locally - but no one can find it, temporary join space and write data?! therefore new write method in networkService...
		final long start = System.currentTimeMillis();
		if( spaceURI!=null && triples != null ) {
			final String ret;
			spaceURI = Util.normalizeSpaceURI(spaceURI, "");
			if( !networkService.getJoinedSpaces().contains(spaceURI) ) {
				new TSException("space " + spaceURI + " must be joined before write can be performed").printStackTrace();
			}
			ret = dataAccessService.write(spaceURI, triples);
			final long timeneeded = System.currentTimeMillis() - start;
			Statistics.addMeasure("write", timeneeded, System.currentTimeMillis());
			return ret;
		}
		throw new TSException("space uri and triples must not be null");
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
}