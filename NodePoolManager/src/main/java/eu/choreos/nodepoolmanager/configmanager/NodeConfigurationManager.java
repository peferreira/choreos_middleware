package eu.choreos.nodepoolmanager.configmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import eu.choreos.nodepoolmanager.chef.ScriptsProvider;
import eu.choreos.nodepoolmanager.datamodel.Node;
import eu.choreos.nodepoolmanager.utils.SshUtil;


public class NodeConfigurationManager {

	private static String INITIAL_RECIPE = "getting-started";
	
    private String updateNodeConfiguration(Node node) throws Exception {
    	return new SshUtil(node.getIp(), node.getUser(), node.getPrivateKeyFile()).runCommand("sudo chef-client\n");
    }
    
    public void initializeNode(Node node) {
 		
    	System.out.println("Waiting for SSH...");
 		SshUtil ssh = new SshUtil(node.getIp(), node.getUser(), node.getPrivateKeyFile());
 		while (!ssh.isAccessible()){
 				System.out.println("Could not connect to " + node.getHostname() +  " using username " + node.getUser() + " yet");
 				System.out.println("Trying again in 5 seconds");
 				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
 		}

 		String command;
		try {
			// bootstrap node
			command = ScriptsProvider.getChefBootstrapScript(node.getPrivateKeyFile(), node.getIp(), node.getUser());
			Process p = Runtime.getRuntime().exec(command);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while (in.readLine() != null) {
			}
			
			// get chef name
			command = ScriptsProvider.getChefName();
			String chefClientName = ssh.runCommand(command);
			node.setChefName(chefClientName);
			System.out.println("nodeName= " + chefClientName);
			
			// install cookbook
			this.installCookbook(node, INITIAL_RECIPE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
 	}
    
    public boolean isInitialized(Node node) throws Exception {
        
    	String createdFile = "chef-getting-started.txt";
    	String returnText = new SshUtil(node.getIp(), node.getUser(), node.getPrivateKeyFile()).runCommand("ls " + createdFile);
    	System.out.println(">>"+returnText.trim()+"<<");
    	return returnText.trim().equals(createdFile);
    }
    
	public String installCookbook(Node node, String cookbook) throws IOException, Exception{
		
		String command = ScriptsProvider.getChefAddCookbook(node.getChefName(), cookbook);
        Runtime.getRuntime().exec(command);
        
        return this.updateNodeConfiguration(node);
	}

}
