package org.ow2.choreos.chors.datamodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Choreography {

	private String id;
	private ChoreographySpec choreographySpec = null;

	// For some mysterious reason, this cannot be initialized here
	// nor at any constructor (JAXB weirdness)
	private List<ChoreographyService> choreographyServices;
	private ChoreographySpec requestedChoreographySpec = null;
	
	public ChoreographyService getDeployedChoreographyServiceByChoreographyServiceUID(
			String choreographyServiceUID) {

		List<ChoreographyService> deployedServices = getChoreographyServices();
		for (ChoreographyService svc : deployedServices) {
			if (choreographyServiceUID.equals(svc.getChoreographyServiceSpec()
					.getName()))
				return svc;
		}
		throw new IllegalArgumentException("Service named "
				+ choreographyServiceUID + " does not exist");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ChoreographySpec getChoreographySpec() {
		return choreographySpec;
	}

	public ChoreographySpec getRequestedChoreographySpec() {
		return requestedChoreographySpec;
	}

	public void setChoreographySpec(ChoreographySpec spec) {
		if (choreographySpec == null) {
			this.choreographySpec = spec;
		}
		this.requestedChoreographySpec = spec;
	}

	public void finishChoreographyEnactment() {
		this.choreographySpec = this.requestedChoreographySpec;
	}

	public List<ChoreographyService> getChoreographyServices() {
		if(choreographyServices == null) {
			this.choreographyServices = new ArrayList<ChoreographyService>();
		}
		return choreographyServices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Choreography other = (Choreography) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Choreography [id=" + id + ", chorSpec="	+ choreographySpec 
				+ ", deployedServices=" + getChoreographyServices()
				+ ", requestedChorSpec=" + requestedChoreographySpec + "]";
	}

	public void addChoreographyService(ChoreographyService choreographyService) {
		getChoreographyServices().add(choreographyService);
	}

	public void setChoreographyServices(
			List<ChoreographyService> choreographyServices) {

		// This does not work, thanks to JAXB...
		/*
		getChoreographyServices().clear();
		for (ChoreographyService service : choreographyServices) {
			this.choreographyServices.add(service);
		}
		*/
		this.choreographyServices = choreographyServices;
	}
}