package eu.choreos.servicedeployer.recipe;

import eu.choreos.servicedeployer.ServiceType;

public class RecipeBuilderFactory {

	public static RecipeBuilder getRecipeBuilderInstance(ServiceType serviceType) {

		switch (serviceType) {
		case WAR:
			return new WARRecipeBuilder();
		default:
			throw new IllegalArgumentException("Service type " + serviceType
					+ " not supported");
		}
	}
}
