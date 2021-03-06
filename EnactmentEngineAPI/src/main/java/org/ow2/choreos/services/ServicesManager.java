package org.ow2.choreos.services;

import org.ow2.choreos.services.datamodel.DeployableService;
import org.ow2.choreos.services.datamodel.DeployableServiceSpec;


public interface ServicesManager {

	/**
	 * 
	 * @param service specification
	 * @return information about how the service was deployed. 
	 * @throws ServiceNotDeployedException if deploy was not possible.
	 */
	public DeployableService createService(DeployableServiceSpec serviceSpec) throws ServiceNotDeployedException;

	/**
	 * 
	 * @param uuid
	 * @return the service representation 
	 * @throws ServiceNotFoundException if ID does not exist
	 * @throws ServiceNotFoundException 
	 */
	public DeployableService getService(String uuid) throws ServiceNotFoundException;
	
	/**
	 * 
	 * @param uuid
	 * @throws ServiceNotDeletedException if it fails
	 */
	public void deleteService(String uuid) 
			throws ServiceNotDeletedException, ServiceNotFoundException;

	/**
	 * 
	 * @param service specification
	 * @return information about how the service was deployed. 
	 * @throws UnhandledModificationException 
	 * @throws ServiceNotDeployedException if deploy was not possible.
	 */
	public DeployableService updateService(DeployableServiceSpec newServiceSpec) throws ServiceNotModifiedException, UnhandledModificationException;
}
