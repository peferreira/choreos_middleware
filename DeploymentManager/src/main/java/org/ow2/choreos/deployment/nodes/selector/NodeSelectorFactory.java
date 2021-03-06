package org.ow2.choreos.deployment.nodes.selector;

import org.ow2.choreos.deployment.Configuration;
import org.ow2.choreos.deployment.nodes.cloudprovider.CloudProvider;

public class NodeSelectorFactory {

	public enum NodeSelectorType {
		ALWAYS_CREATE, ROUND_ROBIN, DEMO
	};

	// singleton
	private static NodeSelector roundRobinSelector, demoSelector;
	
	public static NodeSelector getInstance(CloudProvider cloudProvider) {

		String selector = Configuration.get("NODE_SELECTOR");
		NodeSelectorType type = null;
		try {
			type = NodeSelectorType.valueOf(selector);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(
					"Invalid NODE_SELECTOR in properties file: " + selector);
		}
		return getNodeSelectorInstance(cloudProvider, type);
	}

	private static NodeSelector getNodeSelectorInstance(
			CloudProvider cloudProvider, NodeSelectorType nodeSelectorType) {

		switch (nodeSelectorType) {

		case ALWAYS_CREATE:
			return new AlwaysCreateSelector(cloudProvider);

		case ROUND_ROBIN:
			if (roundRobinSelector == null)
				roundRobinSelector = new RoundRobinSelector(cloudProvider);
			return roundRobinSelector;
			
		case DEMO:
			if (demoSelector == null)
				demoSelector = new DemoSelector(cloudProvider);
			return demoSelector;
			
		default:
			throw new IllegalStateException("Could not choose NodeSelector");
		}
	}

}
