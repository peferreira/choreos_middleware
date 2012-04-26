package eu.choreos.enactment;

import java.util.Set;

import eu.choreos.enactment.context.ChorContextCaster;
import eu.choreos.servicedeployer.datamodel.Service;

public class EnactmentStarter {
	
	private Enacter powsEnacter = EnacterFactory.getPOWSEnacter();
	private Enacter cdEnacter = EnacterFactory.getCDEnacter();
	private Enacter consumeEnacter = EnacterFactory.getSoapConsumeEnacter();
	private Enacter provideEnacter = EnacterFactory.getSoapProvideEnacter();
	
	private Set<Service> powss, consumes;
	
	public void enact() {
		
		System.out.println("Starting airport enactment...");

		this.deployArtifacts();
		this.passContext();
	}

	private void deployArtifacts() {

		// each thread enacts a different group of artifacts
		Thread[] trds = new Thread[4];
		
		trds[0] = new Thread(new Runnable() {
			@Override
			public void run() {
				powss = powsEnacter.enact();				
			}
		});
		trds[0].start();

		trds[1] = new Thread(new Runnable() {
			@Override
			public void run() {
				consumes = consumeEnacter.enact();				
			}
		});
		trds[1].start();
		
		trds[2] = new Thread(new Runnable() {
			@Override
			public void run() {
				provideEnacter.enact();				
			}
		});
		trds[2].start();
		
		trds[3] = new Thread(new Runnable() {
			@Override
			public void run() {
				cdEnacter.enact();			
			}
		});
		trds[3].start();
		
		// wait for all threads finish
		for (Thread t: trds) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void passContext() {
		
		System.out.println("Passing context");
		ChorContextCaster caster = new ChorContextCaster();
		caster.cast(powss, consumes);		
	}
	
	public static void main(String[] args) {

		EnactmentStarter enacter = new EnactmentStarter();
		enacter.enact();
	}

}
