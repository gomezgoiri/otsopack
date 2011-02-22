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
package otsopack.otsoDroid.network.communication.notifications;

import java.util.Enumeration;
import java.util.Hashtable;

import otsopack.otsoMobile.data.ITemplate;
import otsopack.otsoMobile.util.collections.Collection;
import otsopack.otsoMobile.util.collections.HashSet;

public class NotificationContainer implements Collection, INotificationChooser {
	Hashtable notificationsBySelector;
	Hashtable notificationsByURI;
	
	public NotificationContainer() {
		notificationsByURI = new Hashtable();
		notificationsBySelector = new Hashtable();
	}

	public boolean add(Object obj) {
		if( obj==null ) throw new NullPointerException();
		if( obj instanceof INotificationElement ) {
			INotificationElement el = (INotificationElement) obj;
			if( !notificationsByURI.containsKey(el.getURI()) &&
					!notificationsBySelector.containsKey(el.getTemplate()) ) {
				notificationsByURI.put(el.getURI(), el);
				notificationsBySelector.put(el.getTemplate(), el);
				return true;
			} 
			return false;
		}
		
		throw new ClassCastException();
	}

	public boolean addAll(Collection collection) {
		boolean hasChanged = false;
		Enumeration it = collection.elements();
		while( it.hasMoreElements() ) {
			hasChanged |= add( it.nextElement() );
		}
		return hasChanged;
	}

	public void clear() {
		notificationsByURI.clear();
		notificationsBySelector.clear();
	}
	
	public boolean contains(Object obj) {
		return notificationsByURI.contains(obj);
	}

	public boolean containsAll(Collection collection) {
		Enumeration elements = collection.elements();
		while(elements.hasMoreElements()){
			Object nextElement = elements.nextElement();
			if(!notificationsByURI.contains(nextElement))
				return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return notificationsByURI.isEmpty() && notificationsBySelector.isEmpty();
	}

	public Enumeration elements() {
		return notificationsByURI.elements();
	}

	public boolean remove(Object obj) {
		boolean hasChanged = false;
		if( obj instanceof INotificationElement ) {
			INotificationElement el = (INotificationElement) obj;
			hasChanged = notificationsByURI.remove(el.getURI()) != null;
			hasChanged |= notificationsBySelector.remove(el.getTemplate()) != null;
		} else throw new ClassCastException();
		return hasChanged;
	}

	public boolean removeAll(Collection collection) {
		boolean hasChanged = false;
		Enumeration it = collection.elements();
		while( it.hasMoreElements() ) {
			hasChanged |= remove(it.nextElement());
		}
		return hasChanged;
	}

	private boolean removeAll(HashSet collection) {
		boolean hasChanged = false;
		Enumeration it = collection.elements();
		while( it.hasMoreElements() ) {
			hasChanged |= remove(it.nextElement());
		}
		return hasChanged;
	}

	public boolean retainAll(Collection collection) {
		HashSet notRetained = new HashSet();
		Enumeration it = notificationsByURI.elements();
		while( it.hasMoreElements() ) {
			INotificationElement el = (INotificationElement) it.nextElement();
			if( !collection.contains(el) ) {
				notRetained.add(el);
			}
		}
		return removeAll(notRetained);
	}

	public int size() {
		return notificationsByURI.size();
	}

	public Object[] toArray() {
		int position = 0;
		final Object [] newArray = new Object[notificationsByURI.size()];
		final Enumeration elements = notificationsByURI.elements();
		while(elements.hasMoreElements()){
			final Object element = elements.nextElement();
			newArray[position++] = element;
		}
		return newArray;
	}

	public Object[] toArray(Object[] aobj) {
		if(aobj.length < this.notificationsByURI.size())
			return this.toArray();
		
		int position = 0;
		final Enumeration elements = notificationsByURI.elements();
		while(elements.hasMoreElements()){
			final Object element = elements.nextElement();
			aobj[position++] = element;
		}
		
		for(; position < aobj.length; ++position)
			aobj[position] = null;
		
		return aobj;
	}
	
	public INotificationElement get(String uri) {
		return (INotificationElement) notificationsByURI.get(uri);
	}
	
	public INotificationElement get(ITemplate selector) {
		return (INotificationElement) notificationsBySelector.get(selector);
	}
	
	public HashSet getThoseWhichMatch(ITemplate template) {
		HashSet ret = new HashSet();
		Enumeration elements = notificationsBySelector.elements();
		while( elements.hasMoreElements() ) {
			INotificationElement el = (INotificationElement) elements.nextElement();
			if( template.match(el.getTemplate()) ) {
				ret.add(el);
			}
		}
		return ret;
	}
}