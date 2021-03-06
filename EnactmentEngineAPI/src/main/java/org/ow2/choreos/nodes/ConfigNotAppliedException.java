package org.ow2.choreos.nodes;


public class ConfigNotAppliedException extends NPMException {

	private static final long serialVersionUID = -3910285345563830341L;
	private final String configName;

	public ConfigNotAppliedException (String configName) {
		super(null);
		this.configName = configName;
	}
	
	public ConfigNotAppliedException (String configName, String nodeId) {
		super(nodeId);
		this.configName = configName;
	}
	
	@Override
	public String toString() {
		
		String result = "Config " + this.configName + " not applied";
		if (super.getNodeId() != null)
			result += " on node " + super.getNodeId();
		result += ".";
		return result;
	}
	
}
