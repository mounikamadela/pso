package pso;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;

public class PsoImplementation {

	public static void main(String[] args) {
		
		Swarm swarm = new Swarm();

		swarm.runSwarm();	

	}
	
	public void initializeCloud() {
		Datacenter dc = createDatacenter("DataCenter_0");

	}

	private static Datacenter createDatacenter(String name){

		CloudSim cloudSim  = new CloudSim();
		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		//    our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1000;
		

		// 3. Create PEs and add these into a list.
		peList.add(new PeSimple(020000)); // need to store Pe id and MIPS Rating

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 2048; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;

		hostList.add(
    			new HostSimple(
    				ram, bw, storage, peList
    			)
    		); // This is our first machine

		

		Datacenter datacenter = new DatacenterSimple(cloudSim, hostList);

		return datacenter;
	}

	

	
}
