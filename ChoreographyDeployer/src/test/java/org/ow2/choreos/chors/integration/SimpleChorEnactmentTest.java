package org.ow2.choreos.chors.integration;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.ow2.choreos.chors.ChorDeployerImpl;
import org.ow2.choreos.chors.ChoreographyDeployer;
import org.ow2.choreos.chors.ChoreographyNotFoundException;
import org.ow2.choreos.chors.EnactmentException;
import org.ow2.choreos.chors.ModelsForTest;
import org.ow2.choreos.chors.datamodel.ChorSpec;
import org.ow2.choreos.chors.datamodel.Choreography;
import org.ow2.choreos.deployment.services.datamodel.ArtifactType;
import org.ow2.choreos.deployment.services.datamodel.Service;
import org.ow2.choreos.tests.IntegrationTest;
import org.ow2.choreos.utils.LogConfigurator;

import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.WSClient;

/**
 * This test will enact a choreography with two services,
 * with a service depending on the other.
 * 
 * Before the test, start the NPMServer and the ServiceDeployerServer
 *
 * @author leonardo
 *
 */
@Category(IntegrationTest.class)
public class SimpleChorEnactmentTest {

	private ChorSpec chorSpec;
	private ChorSpec spec;
	
	@BeforeClass
	public static void startServers() {
		LogConfigurator.configLog();
	}
	
	@Before
	public void setUp() {
		
		ModelsForTest models = new ModelsForTest(ArtifactType.COMMAND_LINE);
		chorSpec = models.getChorSpec(); 
		spec = models.getChorSpecWithReplicas(2);
	}
	
	@Test
	public void shouldEnactChoreography() throws Exception {
		
		ChoreographyDeployer ee = new ChorDeployerImpl();
		
		String chorId = ee.createChoreography(chorSpec);
		Choreography chor = ee.enact(chorId);

		Service travel = chor.getDeployedServiceByName(ModelsForTest.TRAVEL_AGENCY);
		WSClient client = new WSClient(travel.getUris().get(0) + "?wsdl");
		Item response = client.request("buyTrip");
		String codes = response.getChild("return").getContent();
		
		assertTrue(codes.startsWith("33") && codes.endsWith("--22"));
	}
	
	@Test
	public void shouldEnactChoreographyWithTwoAirlineServices() throws Exception {
		
		ChoreographyDeployer ee = new ChorDeployerImpl();
		
		String chorId = ee.createChoreography(spec);
		Choreography chor = ee.enact(chorId);

		Service airline = chor.getDeployedServiceByName(ModelsForTest.AIRLINE);
		assertEquals(2, airline.getUris().size());
		
		Service travel = chor.getDeployedServiceByName(ModelsForTest.TRAVEL_AGENCY);
		WSClient client = new WSClient(travel.getUris().get(0) + "?wsdl");
		Item response = client.request("buyTrip");
		String codes = response.getChild("return").getContent();
		assertTrue(codes.startsWith("33") && codes.endsWith("--22"));
		
		WSClient client2 = new WSClient(travel.getUris().get(0) + "?wsdl");
		Item response2 = client2.request("buyTrip");
		String codes2 = response2.getChild("return").getContent();
		assertTrue(codes2.startsWith("33") && codes2.endsWith("--22"));
		
		assertFalse(codes.equals(codes2));
		
	}

}
