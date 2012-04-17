/*
 * Copyright (C) 2012 onwards University of Deusto
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
package otsopack.commons.network.subscriptions.bulletinboard.http;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import otsopack.commons.ILayer;
import otsopack.commons.network.communication.event.listener.EventNotification;
import otsopack.commons.network.communication.util.JSONEncoder;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.JSONSerializableConversors;
import otsopack.commons.network.subscriptions.bulletinboard.http.JSONSerializables.TemplateJSON;

public class RemoteNotificator implements ILayer {
	private volatile ExecutorService executor;
	final List<Future<Boolean>> submittedNotifications = new CopyOnWriteArrayList<Future<Boolean>>();
	
	protected RemoteNotificator() {}
	
	@Override
	public void startup() {
		// this.executor = Executors.newFixedThreadPool(MULTICAST_THREADS);
		this.executor = Executors.newCachedThreadPool();
	}

	@Override
	public void shutdown() {
		this.executor.shutdown();
	}

	/**
	 * @param callbackURL
	 * @param notification
	 */
	public void sendNotification(final URI callbackURL, final EventNotification notification) {
		final Future<Boolean> submittedNotification = this.executor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				ClientResource client = new ClientResource(callbackURL);
				try{
					final TemplateJSON notifJson = JSONSerializableConversors.convertToSerializable(notification.getTemplate());
					final JsonRepresentation json = new JsonRepresentation(JSONEncoder.encode(notifJson));
					/*final Representation repr = */client.post(json, MediaType.APPLICATION_JSON);
					//return repr.getText();
					return true;
				} catch (ResourceException e) {
					// TODO with some kind of errors, if something went wrong, chosen can be set to null and try again!
					e.printStackTrace();
				} finally {
					client.release();
				}
				return false;
			}
		});
		
		this.submittedNotifications.add(submittedNotification);
	}
}

class CallbackAndNotification {
	final URI callbackURL;
	final EventNotification notification;
	
	public CallbackAndNotification(URI callbackURL, EventNotification notification) {
		this.callbackURL = callbackURL;
		this.notification = notification;
	}
}