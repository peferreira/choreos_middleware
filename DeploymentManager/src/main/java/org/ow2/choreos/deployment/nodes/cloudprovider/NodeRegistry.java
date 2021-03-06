package org.ow2.choreos.deployment.nodes.cloudprovider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ow2.choreos.nodes.datamodel.Node;

/**
 * Local node registry.
 * 
 * It works as a cache to nodes information provided by the cloud provider.
 * @author leonardo
 *
 */
public class NodeRegistry {
	
	private static NodeRegistry instance = new NodeRegistry();

	private Map<String, Node> nodes = new HashMap<String, Node>();
	
	private NodeRegistry() {
		
	}
	
	public static NodeRegistry getInstance() {
		return instance;
	}
	
	public void putNode(Node node) {
		this.nodes.put(node.getId(), node);
	}
	
	public Node getNode(String nodeId) {
		return this.nodes.get(nodeId);
	}
	
	public void deleteNode(String nodeId) {
		this.nodes.remove(nodeId);
	}
	
	public List<Node> getNodes() {
		return new ArrayList<Node>(this.nodes.values());
	}
}
