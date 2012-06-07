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
package otsopack.commons.network.subscriptions.bulletinboard.http.serializables;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import otsopack.commons.data.NotificableTemplate;
import otsopack.commons.data.TripleLiteralObject;
import otsopack.commons.data.TripleURIObject;
import otsopack.commons.data.WildcardTemplate;
import otsopack.commons.network.subscriptions.bulletinboard.data.RemoteNotificationListener;
import otsopack.commons.network.subscriptions.bulletinboard.data.Subscription;

public class JSONSerializableConversors {

	public static Subscription convertFromSerializable(SubscribeJSON sub) {
		final WildcardTemplate tpl;
		if (sub.tpl==null) {
			tpl = null;
		} else {
			if (sub.tpl.object==null) {
				tpl = WildcardTemplate.createWithNull(sub.tpl.subject, sub.tpl.predicate);
			} else if (sub.tpl.object.startsWith("http://")) {
				tpl = WildcardTemplate.createWithURI(sub.tpl.subject, sub.tpl.predicate, sub.tpl.object);
			} else {
				tpl = WildcardTemplate.createWithLiteral(sub.tpl.subject, sub.tpl.predicate, sub.tpl.object);
			}
		}
		return Subscription.createSubcription(sub.id, sub.getLifetime(), tpl, new RemoteNotificationListener(sub.getCallbackURL()));
	}
	
	public static Subscription[] convertFromSerializable(SubscribeJSON[] subs) {
		final Subscription[] ret = new Subscription[subs.length];
		int i=0;
		for(SubscribeJSON sub: subs) {
			ret[i++] = convertFromSerializable(sub);
		}
		return ret;
	}
	
	public static SubscribeJSON convertToSerializable(Subscription subs) {
		final WildcardTemplate wtpl = (WildcardTemplate) subs.getTemplate();
		final TemplateJSON tpl;
		if (wtpl==null) {
			tpl = null;
		} else {
			final String obj;
			if (wtpl.getObject()==null) {
				obj = null;
			} else if (wtpl.getObject() instanceof TripleLiteralObject ) {
				obj = ((TripleLiteralObject)wtpl.getObject()).getValue().toString();
			} else {
				obj = ((TripleURIObject)wtpl.getObject()).getURI();
			}
			tpl = new TemplateJSON(wtpl.getSubject(), wtpl.getPredicate(), obj);
		}
		
		String callbackURL = null;
		if (subs.getListener() instanceof RemoteNotificationListener) {
			callbackURL = ((RemoteNotificationListener)subs.getListener()).getCallbackURL().toString();
		}
		
		return SubscribeJSON.createSubscription(subs.getID(), tpl, callbackURL, subs.getLifetime());
	}
	
	public static NotificableTemplate convertFromSerializable(TemplateJSON adv) {
		final WildcardTemplate ret;
		if (adv==null) {
			ret = null;
		} else {
			if (adv.object==null) {
				ret = WildcardTemplate.createWithNull(adv.subject, adv.predicate);
			} else if (adv.object.startsWith("http://")){
				ret = WildcardTemplate.createWithURI(adv.subject, adv.predicate, adv.object);
			} else {
				ret = WildcardTemplate.createWithLiteral(adv.subject, adv.predicate, adv.object);
			}
		}
		return ret;
	}
	
	public static TemplateJSON convertToSerializable(NotificableTemplate adv) {
		final WildcardTemplate wtpl = (WildcardTemplate) adv;
		final TemplateJSON ret;
		if (wtpl==null) {
			ret = null;
		} else {
			final String obj;
			if (wtpl.getObject()==null) {
				obj = null;
			} else if (wtpl.getObject() instanceof TripleLiteralObject ) {
				obj = ((TripleLiteralObject)wtpl.getObject()).getValue().toString();
			} else {
				obj = ((TripleURIObject)wtpl.getObject()).getURI();
			}
			ret = new TemplateJSON(wtpl.getSubject(), wtpl.getPredicate(), obj);
		}
		return ret;
	}
	
	public static NotificableTemplate[] convertFromSerializable(TemplateJSON[] advs) {
		final NotificableTemplate[] ret = new NotificableTemplate[advs.length];
		int i=0;
		for(TemplateJSON adv: advs) {
			ret[i++] = convertFromSerializable(adv);
		}
		return ret;
	}
	
	public static TemplateJSON[] convertToSerializable(NotificableTemplate[] advs) {
		final TemplateJSON[] ret = new TemplateJSON[advs.length];
		int i=0;
		for(NotificableTemplate adv: advs) {
			ret[i++] = convertToSerializable(adv);
		}
		return ret;
	}
	
	public static Set<SubscribeJSON> convertToSerializable(Collection<Subscription> subs) {
		final Set<SubscribeJSON> ret = new HashSet<SubscribeJSON>(subs.size());
		for(Subscription s: subs) {
			ret.add(convertToSerializable(s));
		}
		return ret;
	}
}