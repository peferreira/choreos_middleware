package org.ow2.choreos.npm.client;


import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.ow2.choreos.npm.ConfigNotAppliedException;
import org.ow2.choreos.npm.NodeNotCreatedException;
import org.ow2.choreos.npm.NodeNotDestroyed;
import org.ow2.choreos.npm.NodeNotFoundException;
import org.ow2.choreos.npm.NodeNotUpgradedException;
import org.ow2.choreos.npm.NodePoolManager;
import org.ow2.choreos.npm.datamodel.Config;
import org.ow2.choreos.npm.datamodel.Node;
import org.ow2.choreos.npm.datamodel.NodeRestRepresentation;


/**
 * Access Node Pool Manager functionalities through the REST API.
 * 
 * The user of <code>NPMClient</code> does not need to worry with the REST communication.
 * 
 * @author leonardo
 *
 */
public class NPMClient implements NodePoolManager {

	private String host; 

	/**
	 * 
	 * @param host ex: 'http://localhost:9100/nodepoolmanager'
	 * 
	 */
	public NPMClient(String host) {
		
		this.host = host;
	}

	private WebClient setupClient() {
		
		WebClient client = WebClient.create(host);
		
		// remove time out
		// not proud of it!
		HTTPConduit http = (HTTPConduit)WebClient.getConfig(client).getConduit();
		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
		httpClientPolicy.setConnectionTimeout(0);//indefined
		httpClientPolicy.setReceiveTimeout(0);//indefined
		http.setClient(httpClientPolicy);
		
		return client;
	}
	
	@Override
	public Node createNode(Node node) throws NodeNotCreatedException {

		WebClient client = setupClient();
		client.path("nodes");   	
		client.type(MediaType.APPLICATION_XML);
		NodeRestRepresentation nodeRequest = new NodeRestRepresentation(node);
		NodeRestRepresentation nodeRest = null;

        try {
        	nodeRest = client.post(nodeRequest, NodeRestRepresentation.class);
        } catch (WebApplicationException e) {
        	throw new NodeNotCreatedException(node.getId());
        }
        
        return new Node(nodeRest);
	}
	

	
	@Override
	public List<Node> getNodes() {
		throw new NotImplementedException();
	}

	@Override
	public Node getNode(String nodeId) throws NodeNotFoundException {

		WebClient client = setupClient();
		client.path("nodes/" + nodeId);
		NodeRestRepresentation nodeRest = null;
		
		try {
			nodeRest = client.get(NodeRestRepresentation.class);
		} catch (WebApplicationException e) {
			throw new NodeNotFoundException(nodeId);
		}
		
		return new Node(nodeRest);
	}

	@Override
	public void destroyNode(String nodeId) throws NodeNotDestroyed,
			NodeNotFoundException {

		throw new NotImplementedException();
	}
	
	@Override
	public void upgradeNode(String nodeId) throws NodeNotUpgradedException {
		WebClient client = setupClient();
        client.path("nodes");
        client.path(nodeId);
        client.path("upgrade");
        Response response = client.post(null);

        if (response.getStatus() != 200) {
        	throw new NodeNotUpgradedException(nodeId);
        }
	}
	
	@Override
	public Node applyConfig(Config config) throws ConfigNotAppliedException {

		WebClient client = setupClient();
		client.path("nodes/configs");   	
    	NodeRestRepresentation nodeRest = null;
    	
    	try {
    		nodeRest = client.post(config, NodeRestRepresentation.class);
    	} catch (WebApplicationException e) {
    		throw new ConfigNotAppliedException(config.getName());
    	}

        return new Node(nodeRest);
	}

}
