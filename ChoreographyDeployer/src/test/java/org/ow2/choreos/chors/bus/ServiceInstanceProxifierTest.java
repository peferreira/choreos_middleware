package org.ow2.choreos.chors.bus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.ow2.choreos.chors.ModelsForTest;
import org.ow2.choreos.services.datamodel.DeployableService;
import org.ow2.choreos.services.datamodel.PackageType;
import org.ow2.choreos.services.datamodel.Service;
import org.ow2.choreos.services.datamodel.ServiceInstance;
import org.ow2.choreos.services.datamodel.ServiceType;

import esstar.petalslink.com.service.management._1_0.ManagementException;

public class ServiceInstanceProxifierTest {

	private ModelsForTest models = new ModelsForTest(ServiceType.SOAP,
			PackageType.COMMAND_LINE);
	private static final String PROXIFIED_ADDRESS = "http://localhost:8180/services/AirlineServicePortClientProxyEndpoint";

	private ServiceInstance getServiceInstance() {

		Service airlineService = models.getAirlineService();
		ServiceInstance instance = ((DeployableService) airlineService)
				.getInstances().get(0);
		instance.setNativeUri("http://localhost:1234/airline/");

		return instance;
	}

	private EasyESBNode getEsbNode() throws ManagementException {

		EasyESBNode esbNode = mock(EasyESBNodeImpl.class);
		when(esbNode.proxifyService(any(String.class), any(String.class)))
				.thenReturn(PROXIFIED_ADDRESS);
		return esbNode;
	}

	@Test
	public void test() throws ManagementException {

		ServiceInstance svc = this.getServiceInstance();
		EasyESBNode esbNode = this.getEsbNode();

		ServiceInstanceProxifier proxifier = new ServiceInstanceProxifier();
		String proxifiedAddress = proxifier.proxify(svc, esbNode);
		assertEquals(PROXIFIED_ADDRESS, proxifiedAddress);
	}

}
