package org.ow2.choreos.enactment;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ow2.choreos.enactment.datamodel.ChorService;
import org.ow2.choreos.enactment.datamodel.Choreography;
import org.ow2.choreos.servicedeployer.datamodel.Service;
import org.ow2.choreos.servicedeployer.datamodel.ServiceType;
import org.ow2.choreos.utils.LogConfigurator;

import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.ItemImpl;
import eu.choreos.vv.clientgenerator.WSClient;

/**
 * This test enacts a choreography of a single service.
 * 
 * Before the test, start the NPMServer and the ServiceDeployerServer.
 * 
 * @author leonardo
 *
 */
public class SingleServiceEnactmentTest {

	private static final String WEATHER_FORECAST_SERVICE = "WeatherForecastService";
	
	private Choreography chor;
	
	@BeforeClass
	public static void startServers() {
		LogConfigurator.configLog();
	}
	
	@Before
	public void setUp() {
		
		chor = new Choreography();
		ChorService service = new ChorService();
		service.setName(WEATHER_FORECAST_SERVICE);
		service.setCodeUri(AirportProperties.get(WEATHER_FORECAST_SERVICE + ".codeUri"));
		service.setEndpointName(WEATHER_FORECAST_SERVICE.toLowerCase());
		int port = Integer.parseInt(AirportProperties.get(WEATHER_FORECAST_SERVICE + ".port"));
		service.setPort(port);
		service.getRoles().add(WEATHER_FORECAST_SERVICE);
		service.setType(ServiceType.JAR);
		chor.addService(service);
	}
	
	@Test
	public void shouldEnactChoreography() throws Exception {
		
		EnactmentEngine ee = new EnactEngImpl();
		String chorId = ee.createChoreography(chor);
		List<Service> deployedServices = ee.enact(chorId);
		
		Service weather = getWeatherService(deployedServices);
		WSClient client = new WSClient(weather.getUri() + "?wsdl");
		
		Item request = new ItemImpl("getWeatherAt");
		request.addChild("where").setContent("Paris");
		request.addChild("date").setContent("Today");
		Item response = client.request("getWeatherAt", request);
		String humidity = response.getChild("return").getChild("humidityPerc").getContent();
		String temperature = response.getChild("return").getChild("temperatureC").getContent();
		
		assertEquals("50", humidity);
		assertEquals("35", temperature);
	}

	private Service getWeatherService(List<Service> deployedServices) {

		for (Service svc: deployedServices) {
			if (WEATHER_FORECAST_SERVICE.equals(svc.getName()))
				return svc;
		}
		return null;
	}
}
