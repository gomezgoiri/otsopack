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
 */
package otsopack.authn.client.credentials;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocalCredentialsManager {
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock  = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	
	private final Vector<Map.Entry<String, Credentials>> credentials   = new Vector<Map.Entry<String, Credentials>>();
	private final Vector<Map.Entry<String, String>> userIdentifiersURIs = new Vector<Map.Entry<String, String>>();
	
	public LocalCredentialsManager(Map<String, Credentials> originalCredentials, Map<String, String> originalUserIdentifierURIs){
		for(String domain : originalCredentials.keySet())
			setCredentials(domain, originalCredentials.get(domain));
		
		for(String domain : originalUserIdentifierURIs.keySet())
			setUserIdentifierURI(domain, originalUserIdentifierURIs.get(domain));
	}
	
	public LocalCredentialsManager(){}
	
	public void setCredentials(String newDomain, Credentials newCredentials){
		setT(this.credentials, newDomain, newCredentials);
	}
	
	public Credentials getCredentials(String domain){
		return getT(this.credentials, domain);
	}
	
	public void setUserIdentifierURI(String newDomain, String newCredentials){
		setT(this.userIdentifiersURIs, newDomain, newCredentials);
	}
	
	public String getUserIdentifierURI(String domain){
		return getT(this.userIdentifiersURIs, domain);
	}
	
	private <T> void setT(Vector<Map.Entry<String, T>> vector, String newDomain, T newCredentials){
		String bestCoincidence = "";
		int bestCoincidencePos = -1;
		
		final Map.Entry<String, T> newElement = new AbstractMap.SimpleEntry<String, T>(newDomain, newCredentials);

		writeLock.lock();
		try{
			for(int i = 0; i < vector.size(); ++i){
				final Map.Entry<String, T> element = vector.get(i);
				final String elementDomain = element.getKey();
				
				// If the domain already exists, just replace it
				if(elementDomain.equals(newDomain)){
					vector.set(i, newElement);
					return;
				}
				
				// Otherwise check that it is applicable to the current domain
				if(newDomain.startsWith(elementDomain)){
					
					// If it is applicable, check if it is a better option
					// if current bestCoincidence is a subset of elementDomain, it's a better option
					if(elementDomain.startsWith(bestCoincidence)){
						bestCoincidence = elementDomain;
						bestCoincidencePos = i;
					}
				}
			}
			
			if(bestCoincidencePos == -1)
				vector.add(newElement);
			else
				vector.insertElementAt(newElement, bestCoincidencePos);
		}finally{
			writeLock.unlock();
		}
	}
			
	private <T> T getT(Vector<Map.Entry<String, T>> vector, String domain){
		String bestCoincidence = "";
		int bestCoincidencePos = -1;
		
		readLock.lock();
		try{
			
			for(int i = 0; i < vector.size(); ++i){
				final Map.Entry<String, T> element = vector.get(i);
				final String elementDomain = element.getKey();
				if(elementDomain.equals(domain))
					return element.getValue();
				
				// Otherwise check that it is applicable to the current domain
				if(domain.startsWith(elementDomain)){
					
					// If it is applicable, check if it is a better option
					// if current bestCoincidence is a subset of elementDomain, it's a better option
					if(elementDomain.startsWith(bestCoincidence)){
						bestCoincidence = elementDomain;
						bestCoincidencePos = i;
					}
				}
			}
			
			if(bestCoincidencePos == -1)
				return null;
			
			return vector.get(bestCoincidencePos).getValue();
		}finally{
			readLock.unlock();
		}
	}
}
