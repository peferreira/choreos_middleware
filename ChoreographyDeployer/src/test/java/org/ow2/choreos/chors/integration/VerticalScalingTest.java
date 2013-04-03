package org.ow2.choreos.chors.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.ow2.choreos.chors.ChorDeployerImpl;
import org.ow2.choreos.chors.ChoreographyDeployer;
import org.ow2.choreos.chors.ModelsForTest;
import org.ow2.choreos.chors.datamodel.ChorSpec;
import org.ow2.choreos.chors.datamodel.Choreography;
import org.ow2.choreos.deployment.Configuration;
import org.ow2.choreos.deployment.nodes.datamodel.ResourceImpactDefs;
import org.ow2.choreos.deployment.services.datamodel.PackageType;
import org.ow2.choreos.deployment.services.datamodel.Service;
import org.ow2.choreos.tests.IntegrationTest;
import org.ow2.choreos.utils.LogConfigurator;

import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.WSClient;

/**
 *
 * @author tfmend, nelson
 *
 */
@Category(IntegrationTest.class)
public class VerticalScalingTest {

	private ChorSpec smallSpec;
	private ChorSpec mediumSpec;
	//private ChorSpec largeSpec;
	
	/**
	 * Needs to be manually defined with same ip addrress according to 
	 * the first medium ip in the DeploymentManager properties file
	 */
	private static final String MEDIUM_VM_IP = "192.168.122.14";
	
	@BeforeClass
	public static void startServers() {
		LogConfigurator.configLog();
	}
	
	@Before
	public void setUp() {
		
		ModelsForTest models = new ModelsForTest(PackageType.COMMAND_LINE);
		smallSpec = models.getChorSpecWithResourceImpact(ResourceImpactDefs.MemoryTypes.SMALL);
		mediumSpec = models.getChorSpecWithResourceImpact(ResourceImpactDefs.MemoryTypes.MEDIUM);
		//largeSpec = models.getChorSpecWithResourceImpact(ResourceImpactDefs.MemoryTypes.LARGE);
		
	}
	
	@Test
	public void shouldMigrateAirlineServiceFromSmallToMediumMachine() throws Exception {
		
		ChoreographyDeployer ee = new ChorDeployerImpl();
		
		String chorId = ee.createChoreography(smallSpec);
		Choreography chor = ee.enact(chorId);

		Service airline = chor.getDeployedServiceByName(ModelsForTest.AIRLINE);
		Service travel = chor.getDeployedServiceByName(ModelsForTest.TRAVEL_AGENCY);
		
		WSClient client = new WSClient(travel.getUris().get(0) + "?wsdl");
		
		String codes = "";
		
		Item response = client.request("buyTrip");
		codes = response.getChild("return").getContent();

		assertEquals(1, airline.getUris().size());
		assertTrue(codes.startsWith("33") && codes.endsWith("--22"));
		
		
		
		ee.update(chorId, mediumSpec);
		chor = ee.enact(chorId);
		Thread.sleep(4000);
		
		airline = chor.getDeployedServiceByName(ModelsForTest.AIRLINE);
		travel = chor.getDeployedServiceByName(ModelsForTest.TRAVEL_AGENCY);
		
		client = new WSClient(travel.getUris().get(0) + "?wsdl");
		
		response = client.request("buyTrip");
		codes = response.getChild("return").getContent();		
		
		assertEquals(1, airline.getUris().size());
		assertTrue(codes.startsWith("33") && codes.endsWith("--22"));
		
		String actualIp = airline.getUris().get(0);
		
		Matcher m = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}").matcher(actualIp);
		if(m.find()) {
			assertEquals(MEDIUM_VM_IP, m.group());
		}else {
			fail ("Invalid IP");
		}
	}

}
