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
package otsopack.commons.network.communication.comet.event.responses;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = -3776830366381737453L;

	public static final String CODE_TS_AUTHZ                = "TripleSpaces::Authorization";
	public static final String CODE_TS_SPACE_NOT_EXISTS     = "TripleSpaces::SpaceNotExists";
	public static final String CODE_TS_UNSUPPORTED_FORMAT   = "TripleSpaces::UnsupportedFormat";
	public static final String CODE_TS_UNSUPPORTED_TEMPLATE = "TripleSpaces::UnsupportedTemplate";
	// Another TS error, not listed above
	public static final String CODE_TS                      = "TripleSpaces::General";
	public static final String CODE_COMET                   = "Comet";
	public static final String CODE_RESOURCE_NOT_FOUND      = "Resource::NotFound";
	public static final String CODE_RESOURCE_FORBIDDEN      = "Resource::Forbidden";
	public static final String CODE_RESOURCE_CLIENT         = "Resource::Client";
	public static final String CODE_RESOURCE_SERVER         = "Resource::Server";
	// Another TS error, not listed above
	public static final String CODE_RESOURCE                = "Resource::General";
	// TODO: Add more arguments as required
	
	private String errorCode;
	private String errorMessage;
	
	public ErrorResponse(){
	}
	
	public ErrorResponse(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.errorCode == null) ? 0 : this.errorCode.hashCode());
		result = prime
				* result
				+ ((this.errorMessage == null) ? 0 : this.errorMessage
						.hashCode());
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
		ErrorResponse other = (ErrorResponse) obj;
		if (this.errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!this.errorCode.equals(other.errorCode))
			return false;
		if (this.errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!this.errorMessage.equals(other.errorMessage))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ErrorResponse [errorCode=" + this.errorCode + ", errorMessage="
				+ this.errorMessage + "]";
	}
}
