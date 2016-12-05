package CS218Project;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class CS218ProjectEXO {
	private static List<Cloudlet> cloudletList;
	/** The vmlist. */
	private static List<Vm> vmlist;
    private static ArrayList<UE_Context> UE = new ArrayList< UE_Context>();
    private static int sizeUE=1000;
    private static int sizeHD=200;
    private static double lambda=10;
    private static int sizeReq=20000;
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Log.printLine("Starting CloudSimExample2...");
		Log.disable();
		try {
			// First step: Initialize the CloudSim package. It should be called before creating any entities.
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance(); // Calendar whose fields have been initialized with the current date and time.
 			boolean trace_flag = false; // trace events

			/* Comment Start - Dinesh Bhagwat 
			 * Initialize the CloudSim library. 
			 * init() invokes initCommonVariable() which in turn calls initialize() (all these 3 methods are defined in CloudSim.java).
			 * initialize() creates two collections - an ArrayList of SimEntity Objects (named entities which denote the simulation entities) and 
			 * a LinkedHashMap (named entitiesByName which denote the LinkedHashMap of the same simulation entities), with name of every SimEntity as the key.
			 * initialize() creates two queues - a Queue of SimEvents (future) and another Queue of SimEvents (deferred). 
			 * initialize() creates a HashMap of of Predicates (with integers as keys) - these predicates are used to select a particular event from the deferred queue. 
			 * initialize() sets the simulation clock to 0 and running (a boolean flag) to false.
			 * Once initialize() returns (note that we are in method initCommonVariable() now), a CloudSimShutDown (which is derived from SimEntity) instance is created 
			 * (with numuser as 1, its name as CloudSimShutDown, id as -1, and state as RUNNABLE). Then this new entity is added to the simulation 
			 * While being added to the simulation, its id changes to 0 (from the earlier -1). The two collections - entities and entitiesByName are updated with this SimEntity.
			 * the shutdownId (whose default value was -1) is 0    
			 * Once initCommonVariable() returns (note that we are in method init() now), a CloudInformationService (which is also derived from SimEntity) instance is created 
			 * (with its name as CloudInformatinService, id as -1, and state as RUNNABLE). Then this new entity is also added to the simulation. 
			 * While being added to the simulation, the id of the SimEntitiy is changed to 1 (which is the next id) from its earlier value of -1. 
			 * The two collections - entities and entitiesByName are updated with this SimEntity.
			 * the cisId(whose default value is -1) is 1
			 * Comment End - Dinesh Bhagwat 
			 */
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// list one of them to run a CloudSim simulation
			myDatacenterEXO datacenter0 = createDatacenter("Datacenter_0");

			// Third step: Create Broker
			myDatacenterBrokerEXO broker = createBroker(lambda);
			int brokerId = broker.getId();

			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();

			// VM description
			int vmid = 0;
			int mips = 1000;
			long size = 10000; // image size (MB)
			int ram = 512; // vm memory (MB)
			long bw = 1000;
			int pesNumber = 1; // number of cpus
			String vmm = "Xen"; // VMM name

			// create VM
			Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());

			// add the VM to the vmList
			vmlist.add(vm);

			// submit vm list to the broker
			broker.submitVmList(vmlist);

			// Fifth step: Create one Cloudlet
			cloudletList = new ArrayList<Cloudlet>();

			// Cloudlet properties
			int id = 0;
			long length = 0;
			long fileSize = 0;
			long outputSize = 0;
			UtilizationModel utilizationModel = new UtilizationModelFull();

		
			ArrayList<UE_Context> m = new ArrayList<UE_Context>();
			ArrayList<UE_Context> s = new ArrayList<UE_Context>();
			for(UE_Context u:UE){
				int c = u.getCriteria();
				if(c==0||c==2){
					m.add(u);
				}else{
					s.add(u);
				}
			}
			
			
			Random oz=new Random(); 
			int x=sizeReq;
			
			
			while(x!=0){
				//System.out.println(m.size()+" "+s.size());
				double d = Math.random();
				if(d > 0.4){
					UE_Context u=m.get(oz.nextInt(m.size()));
					myCloudlet cloudlet =  new myCloudlet(id, length, pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(vmid);
					cloudletList.add(cloudlet);
				}else{
					UE_Context u=s.get(oz.nextInt(s.size()));
					myCloudlet cloudlet =  new myCloudlet(id, length, pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(vmid);
					cloudletList.add(cloudlet);
				}
				x--;
				id++;
			}
			
			
//			while(x!=0){	
//				double d = Math.random();
//				UE_Context u=UE.get(oz.nextInt(UE.size()));
//				double c=u.getCriteria();				
//				if((c==0||c==2) && d>0.2){
//					myCloudlet cloudlet =  new myCloudlet(id, length, pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
//					cloudlet.setUserId(brokerId);
//					cloudlet.setVmId(vmid);
//					cloudletList.add(cloudlet);
//				}
//				if((c==1||c==3) && d<=0.2){
//					myCloudlet cloudlet =  new myCloudlet(id, length, pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
//					cloudlet.setUserId(brokerId);
//					cloudlet.setVmId(vmid);
//					cloudletList.add(cloudlet);
//				}				
//				x--;
//				id++;
//			}
			
								
			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);

			// Sixth step: Starts the simulation
			CloudSim.startSimulation();

			CloudSim.stopSimulation();

			//Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			printCloudletList(newList);

			Log.printLine("CloudSimExample1 finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}
	
	
	//-------------------------------------------------------
		
	
	@SuppressWarnings("unused")
	private static ArrayList<UE_Context> fillhardrive(HarddriveStorage Harddrive) throws ParameterException{
		Random rn = new Random();
		Random oz=new Random(); 
		
		for(int i=0;i<sizeUE;i++){
			int max=3;
			int min=0;
			int ran= rn.nextInt(max - min + 1) + min;
			UE_Context u =new UE_Context(Integer.toString(i),1,0.0,1,ran);
			UE.add(u);
			
			oz=new Random(); 
			int x=oz.nextInt(1); 
			if(x==1){
				Harddrive.addFile(u);
			}
		}
		
		
		
//		for(int i=0;i<800;i++){
//			int x=oz.nextInt(1);
//			if(x==1){
//				UE.get(i).setCriteria(0);
//			}else
//				UE.get(i).setCriteria(2);
//			
//		}
//		
//		for(int i=800;i<1000;i++){
//			int x=oz.nextInt(1);
//			if(x==1){
//				UE.get(i).setCriteria(1);
//			}else
//				UE.get(i).setCriteria(3);
//		}
		
		return UE;
	}
	
	

	/**
	 * Creates the datacenter.
	 *
	 * @param name the name
	 *
	 * @return the datacenter
	 * @throws ParameterException 
	 */
	private static myDatacenterEXO createDatacenter(String name) throws ParameterException {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1000;

		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

		// 4. Create Host with its id and list of PEs and add them to the list
		// of machines
		int hostId = 0;
		int ram = 2048; // host memory (MB)
		long storage = 1000000; // host storage
		int bw = 10000;

		hostList.add(
			new Host(
				hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerSimple(bw),
				storage,
				peList,
				new VmSchedulerTimeShared(peList)
			)
		); // This is our machine

		// 5. Create a DatacenterCharacteristics object that stores the
		// properties of a data center: architecture, OS, list of
		// Machines, allocation policy: time- or space-shared, time zone
		// and its price (G$/Pe time unit).
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now
	
		HarddriveStorage hd =  new HarddriveStorage("HD0",sizeHD);
		storageList.add(hd);
		fillhardrive(hd);
		
		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		myDatacenterEXO datacenter = null;
		try {
			datacenter = new myDatacenterEXO(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static myDatacenterBrokerEXO createBroker(double lambda) {
		myDatacenterBrokerEXO broker = null;
		try {
			broker = new myDatacenterBrokerEXO("Broker", lambda);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects.
	 *
	 * @param list list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}
	}
		
}
