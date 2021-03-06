package org.ow2.choreos.chef;

import java.util.List;

/**
 * Implementing classes must execute the knife commands
 * 
 * The text output is returned by each method
 * The implementer class can parse the output text to check if is everything OK
 * If not, it is desirable to throw an KnifeException
 * @author leonardo
 *
 */
public interface Knife {

	public KnifeNode node();
	
	public KnifeCookbook cookbook();
	
	public KnifeClient client();
	
	public String bootstrap(String ip, String user, String pKeyFile, List<String> defaultRecipes) throws KnifeException;
    
}
