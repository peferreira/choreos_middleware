package org.ow2.choreos.enactment;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ow2.choreos.enactment.datamodel.ChorServiceSpec;
import org.ow2.choreos.enactment.datamodel.ChorSpec;
import org.ow2.choreos.enactment.datamodel.Choreography;
import org.ow2.choreos.enactment.datamodel.ServiceDependence;
import org.ow2.choreos.servicedeployer.datamodel.Service;
import org.ow2.choreos.servicedeployer.datamodel.ServiceType;
import org.ow2.choreos.utils.LogConfigurator;

import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.ItemImpl;
import eu.choreos.vv.clientgenerator.WSClient;

public class ThalesEnactmentTest {
	private static final String THALES_SERVICE = "Airport";
	
	private ChorSpec chor;
	
	@BeforeClass
	public static void startServers() {
		LogConfigurator.configLog();
	}
	
	@Before
	public void setUp() {
		
		chor = new ChorSpec();
		for (String serviceName: ThalesProperties.SERVICES_NAMES) {
			
			ChorServiceSpec service = new ChorServiceSpec();
			service.setName(serviceName);
			service.setCodeUri(AirportProperties.get(serviceName + ".codeUri"));
			service.setEndpointName(serviceName.toLowerCase());
			int port = Integer.parseInt(AirportProperties.get(serviceName + ".port"));
			service.setPort(port);
			service.getRoles().add(serviceName);
			service.setType(ServiceType.COMMAND_LINE);
			
			List<ServiceDependence> deps = getDependences(serviceName);
			service.setDependences(deps);
			
			chor.addServiceSpec(service);
		}
	}
	
	private List<ServiceDependence> getDependences(String serviceName) {

		List<ServiceDependence> deps = new ArrayList<ServiceDependence>();
		
		String line = AirportProperties.get(serviceName + ".dependences");
		if (line == null)
			return deps;
		
		String[] names = line.split(",");
		for (String name: names) {
			ServiceDependence dep = new ServiceDependence(name, name);
			deps.add(dep);
		}
		
		return deps;
	}

	@Test
	public void shouldEnactChoreography() throws Exception {
		
		EnactmentEngine ee = new EnactEngImpl();
		String chorId = ee.createChoreography(chor);
		Choreography chor = ee.enact(chorId);

		
		//Service groundStaff = deployedServices.get(AIRLINE_GROUND_STAFF_MID);
	}
	
}
