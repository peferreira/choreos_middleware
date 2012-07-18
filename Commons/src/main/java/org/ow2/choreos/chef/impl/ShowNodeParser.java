package org.ow2.choreos.chef.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ow2.choreos.chef.ChefNode;

public class ShowNodeParser {

	private Pattern SIMPLE = Pattern.compile("[a-zA-Z ]+: +(.*)");
	private Pattern RUNLIST_ITEM = Pattern.compile("[a-z]+\\[.+?::.+?\\]");

	/**
	 * Parses the output of the "knife node show" command  
	 * @param output string generated by knife.node().show(nodeName)
	 * @return a ChefNode object or null if parser fails
	 * @throws IOException if parser fails
	 */
	public ChefNode parse(String output) throws IOException {
		
		BufferedReader reader = new BufferedReader(new StringReader(output));

		ChefNode node = new ChefNode();
		
		String line = reader.readLine();
		Matcher matcher = SIMPLE.matcher(line);
		if (matcher.matches()) {
			node.setName(matcher.group(1));
		} else {
			return null;
		}
		
		line = reader.readLine();
		matcher = SIMPLE.matcher(line);
		matcher.matches();
		if (matcher.matches()) {
			node.setEnvironment(matcher.group(1));
		} else {
			return null;
		}
		
		line = reader.readLine();
		matcher = SIMPLE.matcher(line);
		if (matcher.matches()) {
			node.setFqdn(matcher.group(1));
		} else {
			return null;
		}
		
		line = reader.readLine();
		matcher = SIMPLE.matcher(line);
		if (matcher.matches()) {
			node.setIp(matcher.group(1));
		} else {
			return null;
		}
		
		String runlistLine = reader.readLine();
		node.setRunList(parseRunlist(runlistLine));
		
		reader.readLine(); // roles
		reader.readLine(); // recipes
		
		matcher = SIMPLE.matcher(line);
		if (matcher.matches()) {
			node.setPlatform(matcher.group(1));
		} else {
			return null;
		}
		
		return node;
	}

	
	private List<String> parseRunlist(String runlistLine) {

		List<String> runlist = new ArrayList<String>();
		Matcher matcher = RUNLIST_ITEM.matcher(runlistLine);
		while (matcher.find()) {
			runlist.add(matcher.group());
		}
		return runlist;
	}
	
}
