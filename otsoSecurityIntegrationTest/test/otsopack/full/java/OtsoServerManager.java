package otsopack.full.java;

import org.easymock.EasyMock;

import otsopack.commons.IController;
import otsopack.commons.dataaccess.memory.MemoryDataAccess;
import otsopack.commons.network.ICommunication;
import otsopack.full.java.network.communication.OtsoRestServer;
import otsopack.full.java.network.communication.resources.prefixes.PrefixesStorage;

public class OtsoServerManager {
	
	private final int otsoTestingPort;
	protected OtsoRestServer rs;
	protected IController controller;
	
	public OtsoServerManager(int otsoTestingPort){
		this(otsoTestingPort, null);
	}
	
	public OtsoServerManager(int otsoTestingPort, ICommunication multicastProvider){
		this(otsoTestingPort, multicastProvider, true);
	}
	
	public OtsoServerManager(int otsoTestingPort, ICommunication multicastProvider, boolean provideController){
		this.otsoTestingPort = otsoTestingPort;
		if(provideController){
			this.controller = EasyMock.createMock(IController.class);
			//EasyMock.expect(this.controller.getDataAccessService()).andReturn(new FakeDataAccess()).anyTimes();
			EasyMock.expect(this.controller.getDataAccessService()).andReturn(new MemoryDataAccess()).anyTimes();
			EasyMock.replay(this.controller);
		}else
			this.controller = null;
		
		this.rs = new OtsoRestServer(otsoTestingPort, this.controller, multicastProvider);
	}
	
	public void start() throws Exception {
		this.rs.startup();
	}
	
	public void stop() throws Exception {
		this.rs.shutdown();
	}
	
	protected PrefixesStorage getPrefixesStorage(){
		return this.rs.getApplication().getPrefixesStorage();
	}
	
	protected String getOtsoServerBaseURL(){
		return "http://127.0.0.1:" + this.otsoTestingPort;
	}
	

}
