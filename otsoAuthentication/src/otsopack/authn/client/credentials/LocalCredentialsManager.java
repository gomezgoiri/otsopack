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
	
	private final Vector<Map.Entry<String, Credentials>> credentials = new Vector<Map.Entry<String, Credentials>>();
	
	public LocalCredentialsManager(Map<String, Credentials> originalCredentials){
		for(String domain : originalCredentials.keySet())
			setCredentials(domain, originalCredentials.get(domain));
	}
	
	public LocalCredentialsManager(){}
	
	public synchronized void setCredentials(String newDomain, Credentials newCredentials){
		String bestCoincidence = "";
		int bestCoincidencePos = -1;
		
		final Map.Entry<String, Credentials> newElement = new AbstractMap.SimpleEntry<String, Credentials>(newDomain, newCredentials);

		writeLock.lock();
		try{
			for(int i = 0; i < this.credentials.size(); ++i){
				final Map.Entry<String, Credentials> element = this.credentials.get(i);
				final String elementDomain = element.getKey();
				
				// If the domain already exists, just replace it
				if(elementDomain.equals(newDomain)){
					this.credentials.set(i, newElement);
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
				this.credentials.add(newElement);
			else
				this.credentials.insertElementAt(newElement, bestCoincidencePos);
		}finally{
			writeLock.unlock();
		}
	}
	
	public synchronized Credentials getCredentials(String domain){
		String bestCoincidence = "";
		int bestCoincidencePos = -1;
		
		readLock.lock();
		try{
			
			for(int i = 0; i < this.credentials.size(); ++i){
				final Map.Entry<String, Credentials> element = this.credentials.get(i);
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
			
			return this.credentials.get(bestCoincidencePos).getValue();
		}finally{
			readLock.unlock();
		}
	}
	
}
