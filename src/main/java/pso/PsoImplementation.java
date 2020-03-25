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
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;

public class PsoImplementation {

	static CloudSim cloudSim;
	static Vm[][] vmMatrix;
	
	
	public static void main(String[] args) {
		
		initializeCloud();
		cloudSim.start();
		
		Swarm swarm = new Swarm();

		swarm.runSwarm(vmMatrix);	
		
		cloudSim.terminate();

	}
	
	public static void initializeCloud() {
		Datacenter dc = createDatacenter("DataCenter_0",30,30);
		
	}

	private static Datacenter createDatacenter(String name, int sizeOfVms, int sizeOfHosts){

		vmMatrix = new Vm[sizeOfHosts][sizeOfVms];
		cloudSim  = new CloudSim();
		List<Host> hostList = new ArrayList<Host>();

		for(int i = 0; i<sizeOfHosts; i++) {
			for(int j=0; j<sizeOfVms; j++) {
				
				List<Pe> peList = new ArrayList<Pe>();

				int mips = 1000;
				// 3. Create PEs and add these into a list.
				peList.add(new PeSimple(020000)); // need to store Pe id and MIPS Rating

				//4. Create Hosts with its id and list of PEs and add them to the list of machines
				int ram = 2048; //host memory (MB)
				long storage = 1000000; //host storage
				int bw = 10000;
				Host host = new HostSimple(
						ram, bw, storage, peList
					);
				hostList.add(host);
				
				Vm vm = new VmSimple(1000,1);
				vm.setRam(1000).setBw(1000).setSize(1000);
				host.addMigratingInVm(vm);
				vmMatrix[i][j]=vm;				
			}
		}
		
	

		Datacenter datacenter = new DatacenterSimple(cloudSim, hostList);

		return datacenter;
	}

	

	
}
