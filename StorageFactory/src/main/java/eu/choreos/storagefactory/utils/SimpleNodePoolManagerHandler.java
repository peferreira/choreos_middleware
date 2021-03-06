package eu.choreos.storagefactory.utils;

import java.util.ArrayList;
import java.util.List;

public class SimpleNodePoolManagerHandler implements NodePoolManagerHandler{

	private static List<String> deployedRecipe = new ArrayList<String>();
	
	public String createNode(String recipe) {
		deployedRecipe.add(recipe);
		return "choreos-node";
	}

	public String getNode(String nodeId) {
		return "choreos-node";
	}

	public List<String> getNodes() {
		return deployedRecipe;
	}

	public void destroyNode(String id) {
	}

}
