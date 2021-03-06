package org.ow2.choreos;

import org.ow2.choreos.chors.datamodel.ChorSpec;
import org.ow2.choreos.deployment.services.datamodel.PackageType;
import org.ow2.choreos.deployment.services.datamodel.ServiceDependency;
import org.ow2.choreos.deployment.services.datamodel.ServiceSpec;

public class Spec {

	private static final String AIRLINE = "airline";
	private static final String TRAVEL_AGENCY = "travelagency";	
	private static final String AIRLINE_JAR = 
			"http://www.ime.usp.br/~lago/Airline-1.0-SNAPSHOT-jar-with-dependencies.jar";
	private static final String TRAVEL_AGENCY_JAR = 
			"http://www.ime.usp.br/~lago/TravelAgency-1.0-SNAPSHOT-jar-with-dependencies.jar";	
	private static final int AIRLINE_PORT = 1234;
	private static final int TRAVEL_AGENCY_PORT = 1235;	
	
	public static ChorSpec getSpec() {
		
		ChorSpec chorSpec = new ChorSpec(); 
		
		ServiceSpec airline = new ServiceSpec();
		
		airline.setName(AIRLINE);
		airline.setPackageUri(AIRLINE_JAR);
		airline.setEndpointName(AIRLINE);
		airline.setPort(AIRLINE_PORT);
		airline.setPackageType(PackageType.COMMAND_LINE);
		airline.getRoles().add(AIRLINE);
		airline.setNumberOfInstances(2);
		
		chorSpec.addServiceSpec(airline);
		
		ServiceSpec travel = new ServiceSpec();
		travel.setName(TRAVEL_AGENCY);
		travel.setPackageUri(TRAVEL_AGENCY_JAR);
		travel.setEndpointName(TRAVEL_AGENCY);
		travel.setPort(TRAVEL_AGENCY_PORT);
		travel.setPackageType(PackageType.COMMAND_LINE);
		travel.getRoles().add(TRAVEL_AGENCY);
		ServiceDependency dep = new ServiceDependency(AIRLINE, AIRLINE);
		travel.getDependencies().add(dep);
		chorSpec.addServiceSpec(travel); 
		
		return chorSpec;
	}

}
