package pso;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.listeners.EventInfo;

public class PsoImplementation {

	static CloudSim cloudSim;
	static Vm[][] vmMatrix;
	static List<Vm> vmList;
	private static final double TIME_TO_CREATE_NEW_CLOUDLET = 15;
	private static final int HOSTS = 900;
	private static final int HOST_PES = 8;

	private static final int VM_PES = 4;

	private static final int CLOUDLETS = 4;
	private static final int CLOUDLET_PES = 2;
	private static final int CLOUDLET_LENGTH = 10000;

	private static CloudSim simulation;
	private static DatacenterBroker broker0;
	private static List<Cloudlet> cloudletList;
	private static Datacenter datacenter0;

	public static void main(String[] args) {

		PsoImplementation pso = new PsoImplementation();
		pso.createCloud(30, 30);
		Swarm swarm = new Swarm();

		swarm.runSwarm(vmMatrix);

	}

	private void createCloud(int x, int y) {

		simulation = new CloudSim();
		datacenter0 = createDatacenter();

		// Creates a broker that is a software acting on behalf a cloud customer to
		// manage his/her VMs and Cloudlets
		broker0 = new DatacenterBrokerSimple(simulation);

		createVms(x, y);
		cloudletList = createCloudlets();
		broker0.submitVmList(vmList);
		broker0.submitCloudletList(cloudletList);
		broker0.setVmDestructionDelayFunction(vm -> 0.0);
		simulation.addOnClockTickListener(this::createDynamicCloudlet);
		simulation.start();

		final List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
		new CloudletsTableBuilder(finishedCloudlets).build();

	}

	private Vm createVm(final int pes) {
		return new VmSimple(1000, pes).setRam(1000).setBw(1000).setSize(10000)
				.setCloudletScheduler(new CloudletSchedulerTimeShared());
	}

	private void createDynamicCloudlet(final EventInfo evt) {
		if ((int) evt.getTime() == TIME_TO_CREATE_NEW_CLOUDLET) {
			System.out.printf("%n# Dynamically creating 1 Cloudlet and 1 VM at time %.2f%n", evt.getTime());

			Cloudlet cloudlet = createCloudlet();
			cloudletList.add(cloudlet);

			broker0.submitCloudlet(cloudlet);
		}
	}

	private static Cloudlet createCloudlet() {
		UtilizationModel um = new UtilizationModelDynamic(0.2);
		return new CloudletSimple(CLOUDLET_LENGTH, CLOUDLET_PES).setFileSize(1024).setOutputSize(1024)
				.setUtilizationModelCpu(new UtilizationModelFull()).setUtilizationModelRam(um)
				.setUtilizationModelBw(um);
	}

	/**
	 * Creates a Datacenter and its Hosts.
	 */
	private static Datacenter createDatacenter() {
		final List<Host> hostList = new ArrayList<Host>(HOSTS);
		for (int i = 0; i < HOSTS; i++) {
			Host host = createHost();
			hostList.add(host);
		}

		// Uses a VmAllocationPolicySimple by default to allocate VMs
		return new DatacenterSimple(simulation, hostList);
	}

	private static Host createHost() {
		final List<Pe> peList = new ArrayList<Pe>(HOST_PES);
		// List of Host's CPUs (Processing Elements, PEs)
		for (int i = 0; i < HOST_PES; i++) {
			// Uses a PeProvisionerSimple by default to provision PEs for VMs
			peList.add(new PeSimple(1000));
		}

		final long ram = 2048; // in Megabytes
		final long bw = 10000; // in Megabits/s
		final long storage = 1000000; // in Megabytes

		/*
		 * Uses ResourceProvisionerSimple by default for RAM and BW provisioning and
		 * VmSchedulerSpaceShared for VM scheduling.
		 */
		return new HostSimple(ram, bw, storage, peList);
	}

	/**
	 * Creates a list of VMs.
	 */
	private static void createVms(int sizeOfVms, int sizeOfHosts) {
		vmMatrix = new Vm[sizeOfHosts][sizeOfVms];
		vmList = new ArrayList<Vm>();
		for (int i = 0; i < sizeOfHosts; i++) {
			for (int j = 0; j < sizeOfVms; j++) {
				final Vm vm = new VmSimple(1000, VM_PES);
				vm.setRam(512).setBw(1000).setSize(10000);
				vmList.add(vm);
				vmMatrix[i][j] = vm;
			}
		}
	}

	/**
	 * Creates a list of Cloudlets.
	 */
	private static List<Cloudlet> createCloudlets() {
		final List<Cloudlet> list = new ArrayList<Cloudlet>(CLOUDLETS);

		// UtilizationModel defining the Cloudlets use only 50% of any resource all the
		// time
		final UtilizationModelDynamic utilizationModel = new UtilizationModelDynamic(0.5);

		for (int i = 0; i < CLOUDLETS; i++) {
			final Cloudlet cloudlet = new CloudletSimple(CLOUDLET_LENGTH, CLOUDLET_PES, utilizationModel);
			cloudlet.setSizes(1024);
			list.add(cloudlet);
		}

		return list;
	}

}
