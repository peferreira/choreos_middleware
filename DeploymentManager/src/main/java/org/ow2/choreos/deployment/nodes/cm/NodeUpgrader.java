package org.ow2.choreos.deployment.nodes.cm;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.ow2.choreos.deployment.Configuration;
import org.ow2.choreos.nodes.NodeNotUpgradedException;
import org.ow2.choreos.nodes.datamodel.Node;
import org.ow2.choreos.utils.SshCommandFailed;
import org.ow2.choreos.utils.SshUtil;

import com.jcraft.jsch.JSchException;

/**
 * 
 * @author leonardo, cadu, felps
 *
 */
public class NodeUpgrader {

	private Logger logger = Logger.getLogger(NodeUpgrader.class);
	
	
    private static ConcurrentMap<Node, Boolean> updating = new ConcurrentHashMap<Node, Boolean>();
    private static ConcurrentMap<Node, Boolean> needUpdate = new ConcurrentHashMap<Node, Boolean>();

    /**
     * Runs chef-client in a given node
     * @param node
     * @throws JSchException if could not connect into the node
     * @throws NodeNotUpgradedException if chef-client ends in error
     */
    public void upgradeNodeConfiguration(Node node) throws JSchException, NodeNotUpgradedException {
        
    	needUpdate.put(node, true);

        if (updating.containsKey(node) && updating.get(node)) {
        	while (updating.get(node)) {
        		this.sleep(10000);
        	}
            return;
        }

        SshUtil ssh = new SshUtil(node.getIp(), node.getUser(), node.getPrivateKeyFile());
        while (needUpdate.get(node)) {
            
        	needUpdate.put(node, false);
            updating.put(node, true);
            logger.debug("upgrading node " + node);
            try {
				this.runChefClient(ssh);
			} catch (SshCommandFailed e) {
				needUpdate.remove(node);
				updating.remove(node);
				String message = "chef-client returned an error exit status on node " + node.toString();
				logger.error(message);
				throw new NodeNotUpgradedException(node.getId(), message);
			}
            updating.put(node, false);
        }
        
    }


	/**
     * Try to run chef client 5 times
     * 
     * This strategy is carried out since sometimes 
     * we try to ssh the VM when it is not ready yet.
     * 
     * @param ssh
     * @throws JSchException 
     */
    private void runChefClient(SshUtil ssh) throws JSchException, SshCommandFailed {
    	
    	String logFile = Configuration.get("CHEF_CLIENT_LOG");
    	if (logFile == null || logFile.isEmpty()) {
    		logFile = "/tmp/chef-client.log";
    	}
    	
    	final String CHEF_CLIENT_COMMAND = "sudo chef-client --logfile " + logFile;
    	final int MAX_TRIALS = 5;
    	final int SLEEPING_TIME = 5000;
    	int trials = 0;
    	boolean ok = false;
    	
    	while (!ok) {
	    	try {
	    		trials++;
				ssh.runCommand(CHEF_CLIENT_COMMAND);
				ok = true;
			} catch (JSchException e) {
				if (trials >= MAX_TRIALS) {
					throw e;
				}
				sleep(SLEEPING_TIME);
			} catch (SshCommandFailed e) {
				if (trials >= MAX_TRIALS) {
					throw e;
				}
				sleep(SLEEPING_TIME);
			}
    	}
	}
    
    private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			logger.error("Exception at sleeping, should not happen");
		}
    }

}
