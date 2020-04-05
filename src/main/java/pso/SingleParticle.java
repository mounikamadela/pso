package pso;

import java.util.Random;

import org.cloudbus.cloudsim.vms.Vm;

public class SingleParticle {
	private Position pos;
	private Position velocity;
	private Position bestpos;
	private double bestvalue=100;
	
	public SingleParticle(int rangeStart, int rangeEnd) {
		pos=new Position();
		velocity= new Position();
	   setRandomPosition(rangeStart, rangeEnd);
	   bestpos=pos.clone();
	}


	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public Position getVelocity() {
		return velocity;
	}

	public void setVelocity(Position velocity) {
		this.velocity = velocity;
	}

	public Position getBestpos() {
		return bestpos;
	}

	public void setBestpos(Position bestpos) {
		this.bestpos = bestpos;
	}

	public double getBestvalue() {
		return bestvalue;
	}

	public void setBestvalue(double bestvalue) {
		this.bestvalue = bestvalue;
	}

	void updatePosition() {
		this.pos.add(velocity);
	}


	void updatePersonalBest(Vm vm) {		
		double particleBestCpu = vm.getCpuPercentUtilization();
		if (particleBestCpu < bestvalue) {
			bestvalue = particleBestCpu;
			bestpos=pos;
		}
	}

	private static int rand(int beginRange, int endRange) {
		Random r = new java.util.Random();
		return r.nextInt(endRange - beginRange) + beginRange;
	}
	
	 private void setRandomPosition (int beginRange, int endRange) {
	        int x = rand(beginRange, endRange);
	        int y = rand(beginRange, endRange);
	        pos.setX(x);
	        pos.setY(y);
	    }

}