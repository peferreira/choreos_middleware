package br.usp.ime.ccsl.choreos.middleware.roleassignment;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.xml.ws.Endpoint;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.ccsl.choreos.middleware.roleassignment.RoleAssignment;
import br.usp.ime.ccsl.choreos.middleware.roleassignment.RoleAssignmentService;

public class RoleAssignmentServiceTest {
	private static RoleAssignmentService roleAssignmentService;
	private static Endpoint endpoint;

	@BeforeClass
	public static void initWebService() {
		roleAssignmentService = new RoleAssignmentService();
		endpoint = Endpoint.publish("http://localhost:60080/roleAssignment", roleAssignmentService);
	}

	@Test
	public void testAssign() {
		final String uri = "uri";
		final String roleName = "rolename";

		roleAssignmentService.assignRole(new RoleAssignment(uri, roleName));
		List<String> uris = roleAssignmentService.get(roleName);
		assertEquals(uri, uris.get(0));
	}
	
	@Test
	public void testAssignStr() {
		final String uri = "uri";
		final String roleName = "rolename";

		roleAssignmentService.assignRole(roleName, uri);
		List<String> uris = roleAssignmentService.get(roleName);
		assertEquals(uri, uris.get(0));
	}

	@AfterClass
	public static void shutDownWebService() {
		endpoint.stop();
	}
}