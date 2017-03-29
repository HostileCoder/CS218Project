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


public class CS298Project {
	private static List<Cloudlet> cloudletList;
	/** The vmlist. */
	private static List<Vm> vmlist;
    private static ArrayList<UE_Context> UE = new ArrayList< UE_Context>();
    private static int sizeUE=25000;
    private static int sizeRam=3750*200;
    private static double lambda=1400;
    //private static int numReq=84000;//420000
    private static int numReq=420000;
    private static int UEfileSize=200;
    private static double SLARatio=0.0666;
    
    private static int numVM=4;
    private static int numHost=1;
    
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Log.printLine("Starting Project298...");
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
			myDatacenter datacenter0 = createDatacenter("Datacenter_0");

			// Third step: Create Broker
			myDatacenterBroker broker = createBroker(lambda);
			int brokerId = broker.getId();

			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();

			// VM description
			int vmid = 0;
			int mips = 141000;
			long size = 10000; // image size (MB)
			int ram = sizeRam; // vm memory (MB)
			long bw = 1000;
			int pesNumber = 1; // number of cpus
			String vmm = "Xen"; // VMM name
			
			Vm vm =null;
			for(int i=0;i<numVM;i++){
				// create VM
				vm = new myVm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());				
				// add the VM to the vmList
				vmlist.add(vm);
				vmid++;
			}

			// submit vm list to the broker
			broker.submitVmList(vmlist);

			// Fifth step: Create one Cloudlet
			cloudletList = new ArrayList<Cloudlet>();

			// Cloudlet properties
			int id = 0;
			long length = 1;
			long fileSize = 0;
			long outputSize = 0;
			UtilizationModel utilizationModel = new UtilizationModelFull();

		
			ArrayList<UE_Context> l0 = new ArrayList<UE_Context>();
			ArrayList<UE_Context> l1 = new ArrayList<UE_Context>();
			ArrayList<UE_Context> l2 = new ArrayList<UE_Context>();
			ArrayList<UE_Context> l3 = new ArrayList<UE_Context>();
			ArrayList<UE_Context> l4 = new ArrayList<UE_Context>();
			

			for(UE_Context u:UE){
				int c = u.getCriteria();
				if(c==0){
					l0.add(u);
				}else if(c==1){
					l1.add(u);
				}else if(c==2){
					l2.add(u);
				}else if(c==3){
					l3.add(u);
				}else if(c==4){
					l4.add(u);
				}
			}	
	
			
			int UES=100;	//100	750 reqs
			int HO=82;		//82	100 reqs
			int TAU=124;	//124	30 reqs
			int PG=26;		//26	500 reqs
			int AtDe=231;	//231	25 reqs
					
			
			vmid=0;
			Random rn=new Random(); 
			int x=numReq;		
			while(x!=0){
				
				double d = Math.random();

				if(d <= (double)25/1400){
					UE_Context u=l4.get(rn.nextInt(l4.size()));
					myCloudlet cloudlet =  new myCloudlet(id, AtDe, pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(vmid);
					cloudletList.add(cloudlet);				
	
				}else if(d <= (double)30/1400){
					UE_Context u=l2.get(rn.nextInt(l2.size()));
					myCloudlet cloudlet =  new myCloudlet(id, TAU, pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(vmid);
					cloudletList.add(cloudlet);

				}else if(d <= (double)100/1400){
					UE_Context u=l1.get(rn.nextInt(l1.size()));
					myCloudlet cloudlet =  new myCloudlet(id, HO, pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(vmid);
					cloudletList.add(cloudlet);
		
				}else if(d <= (double)500/1400){
					UE_Context u=l3.get(rn.nextInt(l3.size()));
					myCloudlet cloudlet =  new myCloudlet(id, PG , pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(vmid);
					cloudletList.add(cloudlet);
					
				}else {
					UE_Context u=l0.get(rn.nextInt(l0.size()));
					myCloudlet cloudlet =  new myCloudlet(id, UES, pesNumber, fileSize,outputSize, utilizationModel, utilizationModel, utilizationModel,u);
					cloudlet.setUserId(brokerId);
					cloudlet.setVmId(vmid);
					cloudletList.add(cloudlet);		
				}
				
				x--;
				id++;
				vmid= new Random().nextInt(numVM);
			}

		
			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);

			// Sixth step: Starts the simulation
			CloudSim.startSimulation();

			CloudSim.stopSimulation();

			//Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			printCloudletList(newList);

			Log.printLine("CloudSimExample1 finished!");
						
			SimData sd = datacenter0.sd;
			System.out.println(sd);
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}
	
	
	//-------------------------------------------------------
		
	
	@SuppressWarnings("unused")
	private static ArrayList<UE_Context> createUEC() throws ParameterException{
		Random rn = new Random();
		Random oz=new Random(); 
		
		for(int i=0;i<sizeUE;i++){
			int max=3;
			int min=0;
			int ran= rn.nextInt(max - min + 1) + min;
			UE_Context u =new UE_Context(Integer.toString(i),UEfileSize,0.0,1,ran);
			UE.add(u);
			
//			oz=new Random(); 
//			int x=oz.nextInt(1); 
//			if(x==1){
//				Harddrive.addFile(u);
//			}
		}

		return UE;
	}
	
	
	
	private static ArrayList<UE_Context> createUEC2() throws ParameterException{
		Random rn = new Random();
	
		for(int i=0;i<sizeUE;i++){
			int max=4;
			int min=0;
			int ran= rn.nextInt(max - min + 1) + min;
			UE_Context u =new UE_Context(Integer.toString(i),UEfileSize,0.0,1,ran);
			UE.add(u);
		}

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
	private static myDatacenter createDatacenter(String name) throws ParameterException {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<Host> hostList = new ArrayList<Host>();
		
		int hostId = 0;
		for(int i=0;i<numHost;i++){		
				
			    int mips = 700000;				
				// 2. A Machine contains one or more PEs or CPUs/Cores.
				// In this example, it will have only one core.
				List<Pe> peList = new ArrayList<Pe>();
		
				// 3. Create PEs and add these into a list.
				peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		
				// 4. Create Host with its id and list of PEs and add them to the list
				// of machines
				//int hostId = 0;
				int ram = 20000*1000; // host memory (MB)
				long storage = 1000000; // host storage
				int bw = 20000;
		
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
				hostId++;
		}
		
		
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
	
		HarddriveStorage hd =  new HarddriveStorage("HD0",sizeRam);
		storageList.add(hd);
		//fillhardrive(hd);
		createUEC2();
		
		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		myDatacenter datacenter = null;
		try {
			datacenter = new myDatacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
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
	private static myDatacenterBroker createBroker(double lambda) {
		myDatacenterBroker broker = null;
		try {
			broker = new myDatacenterBroker("Broker", lambda);
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
