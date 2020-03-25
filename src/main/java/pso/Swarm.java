package pso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.vms.Vm;

public class Swarm {
	private int rangeStart=0;
	private int rangeEnd=30;
	private int epochs;
	private double bestvalue;
	private Position bestPos;
	public static final double inertia = 0.7345;
	public static final double LEARNING_C1 = 1.45;
	public static final double LEARNING_C2 = 1.45;
	private int totalParticles= 50;
	private Vm[][] vmMatrix;

    public Swarm() {
    	bestPos = new Position();
    }
	
	public void runSwarm(Vm[][] vmMatrix) {
		this.vmMatrix=vmMatrix;
		List<SingleParticle> particleList = init();

		double oldvalue = bestvalue;

		for (int i = 0; i < epochs; i++) {

			if (bestvalue < oldvalue) {
				System.out.println("Global best evaluation " + bestvalue);
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
		
        System.out.println("x = " + bestPos.getX());
      
        System.out.println("y = " + bestPos.getY());

        System.out.println("Final Best Evaluation: " + bestvalue);

	}

	private void updateGBest(SingleParticle particle) {
		
		double particleBestCpu = vmMatrix[particle.getPos().getX()][particle.getPos().getY()].getCpuPercentUtilization();
		if (particleBestCpu < bestvalue) {
			bestPos = particle.getBestpos();
			bestvalue = particleBestCpu;

		}
	}

	private void updateVelocity(SingleParticle particle) {
		Position oldVelocity = particle.getVelocity();
		Position pBest = particle.getBestpos();
		Position gBest = bestPos;
		Position pos = particle.getPos();

		Random random = new Random();
		double r1 = random.nextDouble();
		double r2 = random.nextDouble();

		Position newVelocity = oldVelocity.clone();
		newVelocity.mul(inertia);

		// The second product of the formula.
		pBest.sub(pos);
		pBest.mul(LEARNING_C1);
		pBest.mul(r1);
		newVelocity.add(pBest);

		// The third product of the formula.
		gBest.sub(pos);
		gBest.mul(LEARNING_C2);
		gBest.mul(r2);
		newVelocity.add(gBest);

		particle.setVelocity(newVelocity);

	}

	public List<SingleParticle> init() {

		List<SingleParticle> particles =  new ArrayList<SingleParticle>();
		 for (int i = 0; i <totalParticles; i++) {
	            SingleParticle particle = new SingleParticle(rangeStart, rangeEnd);
	            particles.add(particle);
	            updateGBest(particle);
	        }
	        return particles;

	}

}