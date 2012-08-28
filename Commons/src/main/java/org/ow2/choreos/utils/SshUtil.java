package org.ow2.choreos.utils;

import org.apache.geronimo.mail.util.StringBufferOutputStream;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshUtil {

	private Logger logger = Logger.getLogger(SshUtil.class);
	
	private static final int CONNECTION_TIMEOUT = 5000;
    
	private final String hostname, user, privateKeyFile;
    private Session session;

    public SshUtil(String hostname, String user, String privateKeyFile) {
    	
        this.hostname = hostname;
        this.user = user;
        this.privateKeyFile = privateKeyFile;
    }

    private Session getSession() throws JSchException {
    	
        if (this.session != null && this.session.isConnected()) {
            return this.session;
        }

        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyFile);
        this.session = jsch.getSession(user, hostname);
        this.session.setConfig("StrictHostKeyChecking", "no");
        this.session.setConfig("UserKnownHostsFile", "/dev/null");

        return this.session;
    }

    public boolean isAccessible() {
        
    	Session session = null;
        try {
            // Once upon a time, an old session caused a lot of trouble...
            session = getSession();
            session.connect(CONNECTION_TIMEOUT);
        } catch (JSchException e) {
            return false;
        }

        // We can keep a successful session
        this.session = session;
        return true;
    }

    public String runCommand(String command) throws JSchException {
        return runCommand(command, false);
    }

    public String runCommand(String command, boolean retry) throws JSchException {
        
    	final int SLEEPING_TIME = 5000;
    	String output = null;

        try {
            output = runCommandOnce(command);
        } catch (JSchException e) {
            if (retry) {
            	try {
					Thread.sleep(SLEEPING_TIME);
				} catch (InterruptedException e1) {
				}
                return runCommand(command, retry);
            } else {
                throw e;
            }
        }

        return output;
    }

    public String runCommandOnce(String command) throws JSchException {
        
    	String output = null;
        Session session = getSession();

        try {
            session.connect(CONNECTION_TIMEOUT);

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            StringBuffer sb = new StringBuffer();
            channel.setOutputStream(new StringBufferOutputStream(sb));

            channel.connect();

            while (!channel.isClosed()) {
                try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					logger.error("Sleep exception \"u.u", e);
				}
            }

            channel.disconnect();
            session.disconnect();
            output = sb.toString();
            
        } catch (JSchException e) {
        	logger.debug("Could not connect to " + user + "@" + hostname + " with key "
                    + privateKeyFile);
            throw e;
        }

        return output;
    }
    
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        disconnect();
        super.finalize();
    }
}