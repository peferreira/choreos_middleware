package org.ow2.choreos.chors.context;

import java.util.ArrayList;
import java.util.List;

public class ContextNotSentException extends Exception {

	private static final long serialVersionUID = -8530402048426407353L;

	private String serviceUri, partnerRole, partnerName;
	private List<String> partnerUris = new ArrayList<String>();

	public ContextNotSentException(String serviceUri, String partnerRole,
			String partnerName, List<String> partnerUris) {
		super();
		this.serviceUri = serviceUri;
		this.partnerRole = partnerRole;
		this.partnerName = partnerName;
		this.partnerUris = partnerUris;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getServiceUri() {
		return serviceUri;
	}

	public String getPartnerRole() {
		return partnerRole;
	}

	public List<String> getPartnerUris() {
		return partnerUris;
	}

	@Override
	public String toString() {
		return "ContextNotSentException [serviceUri=" + serviceUri
				+ ", partnerRole=" + partnerRole 
				+ ", partnerName=" + partnerName 
				+ ", partnerUris=" + partnerUris.toString()
				+ "]";
	}
	
}
