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

package otsopack.commons.configuration;

/**
 * Property manager global
 * @author Aitor Gómez Goiri
 */
public class TscMEConfiguration 
{
	/**
	 * Time to wait for network responses to the read primitive when a Graph URI is used.
	 */
	private long timeoutReadURI = 15000;
	
	/**
	 * Time to wait for network responses to the read primitive when a Template is used.
	 */
	private long timeoutReadTemplate = 15000;
	
	/**
	 * Time to wait for network responses to the take primitive when a Graph URI is used.
	 */	
	private long timeoutTakeURI = 15000;
	
	/**
	 * Time to wait for network responses to the take primitive when a Template is used.
	 */	
	private long timeoutTakeTemplate = 15000;
	
	/**
	 * Time to wait for network responses to the query primitive when a Template is used.
	 */	
	private long timeoutQueryTemplate = 15000;
	
	/*public static long TIMEOUT_DISCOVER = 2000;
	public static long MAX_TIMEOUT_DISCOVER = 5000;
	public static long MAX_ATTEMPS_DISCOVER = 10;
	public static long THRESHOLD_DISCOVER = 10;
	public static long MAX_HOP_COUNT = 7; */
	
//	private long DEFAULT_EXPIRATION = Long.MAX_VALUE;
//	private double DEFAULT_LIFETIME = Double.MAX_VALUE;
	
//	private boolean SEND_TRIPLES_ON_LEAVE = false;
	private boolean combineQueryResults = true;
//	private boolean CLEAN_DIR = true;

	private boolean evaluationMode;
		
    /**
     * If before WAITFORCONFIRMATION milliseconds the confirmation to the sent message has not
     * been received, it tries again.
     */
//    public int WAITFORCONFIRMATION = 1000;
       
	private static TscMEConfiguration configuration = null;
	
	public static TscMEConfiguration getConfiguration()
	{
		if (configuration == null)
		{
			configuration = new TscMEConfiguration();
		}
		
		return configuration;
	}
    
    public long getTimeoutReadURI() {
		return timeoutReadURI;
	}

	public void setTimeoutReadURI(long timeoutReadURI) {
		this.timeoutReadURI = timeoutReadURI;
	}

	public long getTimeoutReadTemplate() {
		return timeoutReadTemplate;
	}

	public void setTimeoutReadTemplate(long timeoutReadTemplate) {
		this.timeoutReadTemplate = timeoutReadTemplate;
	}

	public long getTimeoutTakeURI() {
		return timeoutTakeURI;
	}

	public void setTimeoutTakeURI(long timeoutTakeURI) {
		this.timeoutTakeURI = timeoutTakeURI;
	}

	public long getTimeoutTakeTemplate() {
		return timeoutTakeTemplate;
	}

	public void setTimeoutTakeTemplate(long timeoutTakeTemplate) {
		this.timeoutTakeTemplate = timeoutTakeTemplate;
	}

	public long getTimeoutQueryTemplate() {
		return timeoutQueryTemplate;
	}

	public void setTimeoutQueryTemplate(long timeoutQueryTemplate) {
		this.timeoutQueryTemplate = timeoutQueryTemplate;
	}

	public boolean isCombineQueryResults() {
		return combineQueryResults;
	}

	public void setCombineQueryResults(boolean combineQueryResults) {
		this.combineQueryResults = combineQueryResults;
	}

	public void setEvaluationMode(boolean evaluationMode) {
		this.evaluationMode = evaluationMode;
	}
	
	public boolean isEvaluationMode() {
		return evaluationMode;
	}
	
	/**
     * As long as the DEBUG information affects different areas, a better way to specify which
     * specific debug information we want to show, is using this subclass.
     */
    public static class Debug 
    {
	    /**
		 * Display polling debug information.
		 */
	    public static boolean POLLING = false;
	    /**
	     * Display the information related with Jxme actions such as Peer creation, Pipe creation and so on.
	     */
	    //Ok, not only this information is shown, also other things related with coordination procedures are shown (see CoordinationPipe).
	    public static boolean JXME = false;
    	/**
	     * Show how long have been taken connect and other peer operations at JXME communication level.
	     */
	    public static boolean QUANTIFY = false;
	    /**
		 * Display information of the kind of message received by the peer.
		 */
	    public static boolean OPERATION_RCV = false;
	    /**
		 * It shows how much time do a peer need to perform a query, read or take when no timeout is given.
		 */
	    public static boolean WITHOUT_TIMEOUT = false;
	    /**
		 * It shows how much time do a peer need to perform a write, query, read or take locally.
		 */
	    public static boolean STORAGE_OP = false;
	    /**
		 * It shows how much time do a peer need to perform a write, query, read or take through the network.
		 */
	    public static boolean NETWORK_OP = false;
    }

}
