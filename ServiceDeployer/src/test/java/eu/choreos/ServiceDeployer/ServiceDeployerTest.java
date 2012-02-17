package eu.choreos.ServiceDeployer;

import static org.junit.Assert.*;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.choreos.ServiceDeployer.datamodel.ResourceImpact;
import eu.choreos.ServiceDeployer.datamodel.Service;
import eu.choreos.ServiceDeployer.datamodel.ServiceSpec;

import sun.print.resources.serviceui;

public class ServiceDeployerTest {

	private WebClient client;
	protected ServiceDeployer deployer;
	protected static ServiceSpec specWar = new ServiceSpec();
	protected static ServiceSpec specWild = new ServiceSpec();
	protected static ServiceSpec specBpel = new ServiceSpec();
	private static ResourceImpact resourceImpact = new ResourceImpact();
	protected Service service1;
	protected Service service2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		resourceImpact.setCpu("low");
		resourceImpact.setIo("low");
		resourceImpact.setMemory("low");
		resourceImpact.setRegion("BR");

		specWar.setCodeUri("http://content.hccfl.edu/pollock/AJava/WAR/myServletWAR.war");
		specWar.setType("WAR");
		specWar.setResourceImpact(resourceImpact);

		specBpel.setCodeUri("http://content.hccfl.edu/pollock/AJava/WAR/myServletWAR.war");
		specBpel.setType("BPEL");
		specBpel.setResourceImpact(resourceImpact);

		specWild.setCodeUri("http://content.hccfl.edu/pollock/AJava/WAR/myServletWAR.war");
		specWild.setType("WILD");
		specWild.setResourceImpact(resourceImpact);
	}

	@Before
	public void setUp() throws Exception {
		deployer = new ServiceDeployer();
	}

	@Test
	public void shouldDeployAWarServiceInANode() {
		service1 = new Service(specWar);

		service1.setUri("http://this.should.not.work/");
		service1.setId("123456789");

		deployer.deploy(service1);

		client = WebClient.create(service1.getUri());
		assertEquals(200, client.get().getStatus());
	}

	@Test
	public void shouldDeployABPELServiceInANode() {
		service1 = new Service(specBpel);

		service1.setUri("http://this.should.not.work/");
		service1.setId("123456789");

		deployer.deploy(service1);

		client = WebClient.create(service1.getUri());
		assertEquals(200, client.get().getStatus());
	}

	@Test
	public void shouldDeployAWildServiceInANode() {
		service1 = new Service(specWild);

		service1.setUri("http://this.should.not.work/");
		service1.setId("123456789");

		deployer.deploy(service1);

		client = WebClient.create(service1.getUri());
		assertEquals(200, client.get().getStatus());
	}

	@Test
	public void shouldGetServiceFromServiceID() {
		service1 = new Service(specWar);
		service1.setId("123456789");
		deployer.deploy(service1);

		assertEquals(service1, deployer.getService("123456789"));
	}

	@Test
	public void shouldGetSpecificServiceFromAServiceGroupGivenTheServiceID() {
		service1 = new Service(specWar);
		service1.setId("123456789");
		deployer.deploy(service1);

		service2 = new Service(specWar);
		service2.setId("987654321");
		deployer.deploy(service1);

		assertEquals(service1, deployer.getService("123456789"));
	}

	@Test
	public void shouldUpdateAService() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldUpdateTheCorrectServiceGivenItsID() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldDeleteAService() {
		service1 = new Service(specWar);
		service1.setId("123456789");
		service1.setUri("http://this.should.not.work/");

		deployer.deploy(service1);

		service2 = new Service(specWar);
		service2.setId("987654321");
		service2.setUri("http://this.should.not.work/");

		deployer.deploy(service1);
		deployer.deleteService("123456789");

		assertTrue(deployer.getService("123456789") == null);

		client = WebClient.create(service1.getUri());
		assertEquals(200, client.get().getStatus());
	}

	@Test
	public void ShouldOnlyDeleteTheCorrectService() {
		service1 = new Service(specWar);
		service1.setId("123456789");
		deployer.deploy(service1);

		service2 = new Service(specWar);
		service2.setId("987654321");
		deployer.deploy(service1);

		deployer.deleteService("123456789");
		assertTrue("Did not delete the requested node",
				deployer.getService("123456789") == null);
		assertTrue("Deleted an unrequested node or all of them",
				deployer.getService("987654321") != null);
	}

}
