package org.ow2.choreos.chors.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.ow2.choreos.services.datamodel.ServiceSpec;

public class ChoreographyServiceSpec {
	private ServiceSpec serviceSpec;

	private String name;
	private String owner;
	private String group;
	private List<String> roles;
	private List<ChoreographyServiceDependency> dependencies;
	
	public ChoreographyServiceSpec() {
		
	}

	public ChoreographyServiceSpec(ServiceSpec serviceSpec, String owner,
			String group, List<String> roles, String choreographyServiceUID) {
		this.serviceSpec = serviceSpec;
		this.owner = owner;
		this.group = group;
		this.roles = roles;
		this.name = choreographyServiceUID;
	}

	public ChoreographyServiceSpec(ServiceSpec serviceSpec, String owner,
			String group, List<String> roles,
			List<ChoreographyServiceDependency> dependencies,
			String name) {
		this.serviceSpec = serviceSpec;
		this.owner = owner;
		this.group = group;
		this.roles = roles;
		this.dependencies = dependencies;
		this.name = name;
	}

	public ServiceSpec getServiceSpec() {
		return serviceSpec;
	}

	public void setServiceSpec(ServiceSpec serviceSpec) {
		this.serviceSpec = serviceSpec;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void addRole(String role) {
		if (this.roles == null)
			this.roles = new ArrayList<String>();
		roles.add(role);
	}

	public void addAllRoles(List<String> roles) {
		if (this.roles == null)
			this.roles = new ArrayList<String>();
		roles.addAll(roles);
	}

	public List<ChoreographyServiceDependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<ChoreographyServiceDependency> dependencies) {
		this.dependencies = dependencies;
	}

	public void addDependency(ChoreographyServiceDependency dependency) {
		if (this.dependencies == null)
			this.dependencies = new ArrayList<ChoreographyServiceDependency>();
		this.dependencies.add(dependency);
	}

	public void addDependencies(List<ChoreographyServiceDependency> dependencies) {
		if (this.dependencies == null)
			this.dependencies = new ArrayList<ChoreographyServiceDependency>();
		this.dependencies.addAll(dependencies);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((name == null) ? 0
						: name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result
				+ ((dependencies == null) ? 0 : dependencies.hashCode());
		result = prime * result + ((serviceSpec == null) ? 0 : serviceSpec.hashCode());
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
		ChoreographyServiceSpec other = (ChoreographyServiceSpec) obj;

		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;

		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;

		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;

		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;

		if (dependencies == null) {
			if (other.dependencies != null)
				return false;
		} else if (!dependencies.equals(other.dependencies))
			return false;
		
		if (serviceSpec == null) {
			if(other.serviceSpec != null)
				return false;
		} else if (!serviceSpec.equals(other.serviceSpec))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "ChoreographyServiceSpec [name=" + name
				+ ", owner=" + owner + ", group=" + group + ", roles=" + roles
				+ ", dependencies=" + dependencies + ", serviceSpec="
				+ serviceSpec;
	}
}