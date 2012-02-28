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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */
package otsopack.commons.network.communication.comet.event;

public class EventPetition {
	private final String sessionId;
	private final Event event;
	
	public EventPetition(String sessionId,  Event event){
		this.sessionId = sessionId;
		this.event = event;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public Event getEvent() {
		return this.event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.event == null) ? 0 : this.event.hashCode());
		result = prime * result
				+ ((this.sessionId == null) ? 0 : this.sessionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventPetition other = (EventPetition) obj;
		if (this.event == null) {
			if (other.event != null)
				return false;
		} else if (!this.event.equals(other.event))
			return false;
		if (this.sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!this.sessionId.equals(other.sessionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RequestEvent [sessionId=" + this.sessionId + ", event="
				+ this.event + "]";
	}
}
