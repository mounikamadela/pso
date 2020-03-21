package pso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Swarm {
	private int rangeStart;
	private int rangeEnd;
	private int epochs;
	private double bestvalue;
	private Position bestPos;
	
	
	public void runSwarm() {
		List<SingleParticle> particleList = init();
		
		double oldvalue = bestvalue;
		
		for(int i = 0; i < epochs; i++) {
			
			if(bestvalue < oldvalue) {
				System.out.println("Global best evaluation " +bestvalue);
				oldvalue = bestvalue;
				
			}
			
			for (SingleParticle p : particleList) {
				p.updatePBest();
				updateGBest(p);
			}
			
			for (SingleParticle p : particleList) {
				updateVelocity(p);
				p.updatePosition();
				
			}
			
		}
		
	}
	
	private void updateGBest(SingleParticle particle) {
		if(particle.getBestvalue() < bestvalue) {
			bestPos = particle.getBestpos();
			bestvalue = particle.getBestvalue();
			
		}	
	}
	
	private void updateVelocity (SingleParticle particle) {
		double oldVelocity = particle.getVelocity();
		Position pBest = particle.getBestpos();
		Position gBest = bestPos;
		Position pos = particle.getPos();
		
		Random random = new Random();
		double r1 = random.nextDouble();
		double r2 = random.nextDouble();
		
		double newVelocity = oldVelocity;
		
		particle.setVelocity(newVelocity);
			
	}
	public List<SingleParticle> init(){
		
		return new ArrayList();
		
		
		
	}

}