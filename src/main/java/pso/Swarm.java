package pso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.cloudbus.cloudsim.vms.Vm;

public class Swarm {
	public static final int GRID_SIZE=30;
	private int rangeStart=0;
	private int rangeEnd=GRID_SIZE-1;
	private int epochs=50;
	private double bestvalue=100;
	private Position bestPos;
	public static final double inertia = 0.7345;
	public static final double LEARNING_C1 = 1.45;
	public static final double LEARNING_C2 = 1.45;
	private int totalParticles= 50;
	private Vm[][] vmMatrix;

    public Swarm() {
    	
    }
    
    public void runSingleSwarm(List<SingleParticle> particleList) {
    	Date startTime = new Date();
		System.out.println("Starting time:"+startTime);
    	for (int i = 0; i < epochs; i++) {

			for (SingleParticle p : particleList) {
				if(p.getPos().getX()< GRID_SIZE && p.getPos().getY()<GRID_SIZE){
					p.updatePersonalBest(vmMatrix[p.getPos().getX()][p.getPos().getY()]);
					updateGBest(p);
				}
			}

			for (SingleParticle p : particleList) {
				updateVelocity(p);
				p.updatePosition();

			}
		}
		
		Date endTime = new Date();
		
		System.out.println("Time taken for evaluation:" + (endTime.getTime()-startTime.getTime()));
		System.out.println("Best VM with less percentage of CPU utilization at position: ("+bestPos.getX()+"," +bestPos.getY()+")" );

    }
	
	public void runSwarm(Vm[][] vmMatrix, int threadSize) {
	
		this.vmMatrix=vmMatrix;
	

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (int i=0;i<threadSize; i++) {
		executorService.execute(new Runnable() {
		    public void run() {		
		    	List<SingleParticle> particleList = init();
		    	runSingleSwarm(particleList);
		    }
		});
		}

	}

	private void updateGBest(SingleParticle particle) {
		
		double particleBestCpu = vmMatrix[particle.getPos().getX()][particle.getPos().getY()].getCpuPercentUtilization();
		if (particleBestCpu <= bestvalue) {
			bestPos = particle.getPos().clone();
			bestvalue = particleBestCpu;

		}
	}

	private void updateVelocity(SingleParticle particle) {
		Position oldVelocity = particle.getVelocity();
		Position pBest = particle.getBestpos();
		Position gBest = bestPos;
		Position pos = particle.getPos();

		/*
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

		particle.setVelocity(newVelocity);*/

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