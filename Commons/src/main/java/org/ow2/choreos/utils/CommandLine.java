package org.ow2.choreos.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class CommandLine {
	
	private static Logger logger = Logger.getLogger(CommandLine.class);

	public static String run(String command) {
		return run(command, false);
	}
	
	public static String run(String command, boolean verbose) {
		return run(command, ".", verbose);
	}
	
	public static String run(String command, String workingDirectory) {
		return run(command, workingDirectory, false);
	}
	
	public static String run(String command, String workingDirectory, boolean verbose) {
		String commandReturn = "";
		File wd = new File(workingDirectory);

		try {
			if (verbose) {
				logger.info(command);
			}
			Process p = Runtime.getRuntime().exec(command, null, wd);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				commandReturn = commandReturn + line + '\n';
				if (verbose) {
					logger.info(commandReturn);
				}
			}
		} catch (IOException e) {
			logger.error("Error while executing " + command, e);
			e.printStackTrace();
		}

		return commandReturn;
	}

}