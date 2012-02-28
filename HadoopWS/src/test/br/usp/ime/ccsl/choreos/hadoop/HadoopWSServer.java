package br.usp.ime.ccsl.choreos.hadoop;


import java.net.URI;
import java.net.URISyntaxException;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

public class HadoopWSServer implements Runnable {

	public static final String SERVER_ADDRESS = "http://localhost:8080/";
	private static boolean running = false;

	public static void start() throws InterruptedException {
		new Thread(new HadoopWSServer()).start();
		while (!running) {
			Thread.sleep(1);
		}

	}

	public static void stop() {
		running = false;
	}

	public void run() {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(HadoopWS.class);
		sf.setAddress(SERVER_ADDRESS);
		sf.create();
		System.out.println("Starting server...");
		running = true;

		while (running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Stopping server...");

	}

	public static void main(String[] args) throws InterruptedException {
		HadoopWSServer.start();
	}

}