package CS218Project;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

public class myDatacenter extends Datacenter{

	private HarddriveStorage HD0 = null;
	private VRAM2 RAM0=null;
	private ArrayList <UE_Context> Evict=new ArrayList <UE_Context>();
	private ArrayList<UE_Context> CacheState = new ArrayList<UE_Context>();
	private AHP ahp =new AHP(4);
	private int agingFac=0;
	
	private int evicted=0;
	private double used=0;
	private double capacity=0;
	private double freespace=0;
	private double sumWeight=0;
	private UE_Context file=null;
	private Ratio ratio=new Ratio(Math.pow(10, -3));
	private History history = new History(0,0,0,ratio.getRatio());
	private int totalEvicts=0;

	public  int missInsertL=0;
	public  int missNoInsertL=0;
	public  int missInsertEvictL=0;
	
	public  int missInsertH=0;
	public  int missNoInsertH=0;
	public  int missInsertEvictH=0;

	
	private PrintWriter outputWriter = new PrintWriter ("file.txt");
	private List<myVm> vmlist = null;
	private Host host0=null;
	private RamProvisioner ramProvisioner=null;
	
	private int missInsert=0;
	private int missForceInsert=0;
	private int missInsertEvict=0;
	private ArrayList <History> records = new ArrayList<History>();
	
	private int printing=1;
	
	private String method="l";
	
	
	public myDatacenter(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy,
			List<Storage> storageList, double schedulingInterval) throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
		// TODO Auto-generated constructor stub	
		HD0=(HarddriveStorage) storageList.get(0);	
	
		ahp.setWeight(0,1,5);
		ahp.setWeight(0,2,5);
		ahp.setWeight(0,3,5);
		ahp.setWeight(1,2,5);
		ahp.setWeight(1,3,5);
		ahp.setWeight(2,3,5);
		ahp.findWeight();
				
	}
	
	@Override
	public void processEvent(SimEvent ev) {
		int srcId = -1;

		switch (ev.getTag()) {
		// Resource characteristics inquiry
			case CloudSimTags.RESOURCE_CHARACTERISTICS:								
				srcId = ((Integer) ev.getData()).intValue();
				sendNow(srcId, ev.getTag(), getCharacteristics());
				break;

			// Resource dynamic info inquiry
			case CloudSimTags.RESOURCE_DYNAMICS:
				srcId = ((Integer) ev.getData()).intValue();
				sendNow(srcId, ev.getTag(), 0);
				break;

			case CloudSimTags.RESOURCE_NUM_PE:
				srcId = ((Integer) ev.getData()).intValue();
				int numPE = getCharacteristics().getNumberOfPes();
				sendNow(srcId, ev.getTag(), numPE);
				break;

			case CloudSimTags.RESOURCE_NUM_FREE_PE:
				srcId = ((Integer) ev.getData()).intValue();
				int freePesNumber = getCharacteristics().getNumberOfFreePes();
				sendNow(srcId, ev.getTag(), freePesNumber);
				break;

			// New Cloudlet arrives
			case CloudSimTags.CLOUDLET_SUBMIT:
				processCloudletSubmit(ev, false);
				break;

			// New Cloudlet arrives, but the sender asks for an ack
			case CloudSimTags.CLOUDLET_SUBMIT_ACK:
				processCloudletSubmit(ev, true);
				break;

			// Cancels a previously submitted Cloudlet
			case CloudSimTags.CLOUDLET_CANCEL:
				processCloudlet(ev, CloudSimTags.CLOUDLET_CANCEL);
				break;

			// Pauses a previously submitted Cloudlet
			case CloudSimTags.CLOUDLET_PAUSE:
				processCloudlet(ev, CloudSimTags.CLOUDLET_PAUSE);
				break;

			// Pauses a previously submitted Cloudlet, but the sender
			// asks for an acknowledgement
			case CloudSimTags.CLOUDLET_PAUSE_ACK:
				processCloudlet(ev, CloudSimTags.CLOUDLET_PAUSE_ACK);
				break;

			// Resumes a previously submitted Cloudlet
			case CloudSimTags.CLOUDLET_RESUME:
				processCloudlet(ev, CloudSimTags.CLOUDLET_RESUME);
				break;

			// Resumes a previously submitted Cloudlet, but the sender
			// asks for an acknowledgement
			case CloudSimTags.CLOUDLET_RESUME_ACK:
				processCloudlet(ev, CloudSimTags.CLOUDLET_RESUME_ACK);
				break;

			// Moves a previously submitted Cloudlet to a different resource
			case CloudSimTags.CLOUDLET_MOVE:
				processCloudletMove((int[]) ev.getData(), CloudSimTags.CLOUDLET_MOVE);
				break;

			// Moves a previously submitted Cloudlet to a different resource
			case CloudSimTags.CLOUDLET_MOVE_ACK:
				processCloudletMove((int[]) ev.getData(), CloudSimTags.CLOUDLET_MOVE_ACK);
				break;

			// Checks the status of a Cloudlet
			case CloudSimTags.CLOUDLET_STATUS:
				processCloudletStatus(ev);
				break;

			// Ping packet
			case CloudSimTags.INFOPKT_SUBMIT:
				processPingRequest(ev);
				break;

			case CloudSimTags.VM_CREATE:
				processVmCreate(ev, false);
				break;

			case CloudSimTags.VM_CREATE_ACK:
				processVmCreate(ev, true);
				
				host0= this.getHostList().get(0);
				vmlist=host0.getVmList();		
				ramProvisioner=host0.getRamProvisioner();
				
				break;

			case CloudSimTags.VM_DESTROY:
				processVmDestroy(ev, false);
				break;

			case CloudSimTags.VM_DESTROY_ACK:
				processVmDestroy(ev, true);
				break;

			case CloudSimTags.VM_MIGRATE:
				processVmMigrate(ev, false);
				break;

			case CloudSimTags.VM_MIGRATE_ACK:
				processVmMigrate(ev, true);
				break;

			case CloudSimTags.VM_DATA_ADD:
				processDataAdd(ev, false);
				break;

			case CloudSimTags.VM_DATA_ADD_ACK:
				processDataAdd(ev, true);
				break;

			case CloudSimTags.VM_DATA_DEL:
				processDataDelete(ev, false);
				break;

			case CloudSimTags.VM_DATA_DEL_ACK:
				processDataDelete(ev, true);
				break;

			case CloudSimTags.VM_DATACENTER_EVENT:
				updateCloudletProcessing();
				checkCloudletCompletion();
				break;

			// other unknown tags are processed by this method
			default:
				processOtherEvent(ev);
				break;
		}
	}
	
	protected void processCloudletSubmit(SimEvent ev, boolean ack) {
	

		//System.out.println(vmlist.get(0).getId());
	
		double time = ev.eventTime();			
		myCloudlet cl = (myCloudlet) ev.getData();
		
		
		int UserId = cl.getUserId();
		int VmId = cl.getVmId();
		Host Host = getVmAllocationPolicy().getHost(VmId, UserId);
		Vm VM = Host.getVm(VmId, UserId);
		RAM0= ((myVm) VM).getVram();
		history = ((myVm) VM).getHistory();
		CacheState = ((myVm) VM).getCacheState();
		
		
		file = cl.getUE();			
		int result= handleRequest(time);
		history.incTotalRequest();		
		history.incIdvHit(file.getCriteria(),result);
				
		if(result==0){
			//Log.printLine("Cache:Missed!");
		}else if(result==1){
			//Log.printLine("Cache:Hit!");
		}else if(result==-1){
			//System.out.println("Cache:Missed!, No insertion");
		}else if(result==-2){
			//System.out.println("Cache:Missed!, with eviction and insertion"+" "+evicted);
		}
		
		
		//Cache:Hit!
		if(result==1){
			history.incTotalHit();				
		}
		

		//0:Cache:Missed!
		if(result==0){    
			history.incTotalMiss();
			
			history.incNumInsert();	
			history.incIdvInsert(file.getCriteria(),1);

			history.addWrites(1);
			history.incIdvWrite(file.getCriteria(),1);			
	
			missInsert++;
		}
		
		//-2:Cache:Missed!, with eviction and insertion
		if(result==-2){    
			history.incTotalMiss();
			
			history.incNumInsert();	
			history.incIdvInsert(file.getCriteria(),1);

			history.addWrites(evicted);
			history.incIdvWrite(file.getCriteria(),evicted);			

			missInsertEvict=missInsertEvict+evicted;
			totalEvicts=totalEvicts+evicted;
		}
		
		
		//Cache:Missed!, No insertion
		if(result==-1){    
			history.incTotalMiss();
		
			history.addWrites(1);
			history.incIdvWrite(file.getCriteria(),1);			
		
			history.incNumInsert();	
			history.incIdvInsert(file.getCriteria(),1);
			
			missForceInsert++;
		}
		
		
		
		if(RAM0.getFreeSpace()<=0&&printing==1)
		System.out.println(time+" "+history+
				" 		L1:"+history.L1H/history.L1+
				" 		L2:"+history.L2H/history.L2+
				" 		L3:"+history.L3H/history.L3+
				" 		L4:"+history.L4H/history.L4+	
				" 		"+history.L1W+
				" 		"+history.L2W+
				" 		"+history.L3W+
				" 		"+history.L4W
				);	 
		
		if(RAM0.getFreeSpace()<=0)
		outputWriter.write(time+
				" 	"+missInsert+
				" 	"+missForceInsert+
				" 	"+missInsertEvict+
				" 	"+totalEvicts+
				" 	"+history.writes+
				" 	"+history.L1H/history.L1+
				" 	"+history.L2H/history.L2+
				" 	"+history.L3H/history.L3+
				" 	"+history.L4H/history.L4+	
				"\n");	

		
		cl.setResourceParameter(
                getId(), getCharacteristics().getCostPerSecond(), 
                getCharacteristics().getCostPerBw());

		int userId = cl.getUserId();
		int vmId = cl.getVmId();

		// time to transfer the files
		double fileTransferTime = predictFileTransferTime(cl.getRequiredFiles());

		Host host = getVmAllocationPolicy().getHost(vmId, userId);
		Vm vm = host.getVm(vmId, userId);
		CloudletScheduler scheduler = vm.getCloudletScheduler();
		double estimatedFinishTime = scheduler.cloudletSubmit(cl, fileTransferTime);

		// if this cloudlet is in the exec queue
		if (estimatedFinishTime > 0.0 && !Double.isInfinite(estimatedFinishTime)) {
			estimatedFinishTime += fileTransferTime;
			send(getId(), estimatedFinishTime, CloudSimTags.VM_DATACENTER_EVENT);
		}
		
		
	}
	

	private int handleRequest(double time) {		
//		used= HD0.getCurrentSize();
//		capacity= HD0.getCapacity();
		
		evicted=0;		
		used=RAM0.getUsed();
		capacity=RAM0.getSize();
		
		//System.out.println("Used:"+used+" Capacity:"+capacity);			
		
		if(method.equals("e")){
			findEXDWeight(file.getEXDScore(),ratio.getRatio(),time,file.getEXDTime());
		}else if(method.equals("a")||method.equals("a1")){
			findEXDAHPWeight(file.getEXDScore(),ratio.getRatio(),time,file.getEXDTime());
		}else if(method.equals("l")){
			findLFUWeight();
		}else{
			findLFUWeight();
		}
		
		
		if(CacheState.contains(file)){
			return 1;
		}
		
		if(file.getSize()+used<=capacity){
			used=used+file.getSize();
			insert(file);
			RAM0.addFile(file);
			evicted=0;
			return 0;
		}
					
		sumWeight=0;
		freespace=capacity-used;
					
		for(UE_Context x : CacheState) {
			if(sumWeight+x.getWeight()<file.getWeight()){
				 sumWeight = sumWeight + x.getWeight();
				 freespace = freespace + x.getSize();
				 Evict.add(x);
				 //System.out.println(x.getName()+" chosen for eviction");
				 if(freespace>=file.getSize()){
					 break;
				 }						
			}
		}
							
		if(freespace<file.getSize()){
			//System.out.println("Cache:Missed!, No insertion");
			UE_Context x=CacheState.get(0);
			RAM0.removeFile(x);
			remove(x);
			RAM0.addFile(file);
			insert(file);
			return -1;
		}
					
		//remove from CachedState
		for(UE_Context u: Evict){
			remove(u);							
		}	
		
		//remove from Cache
		for(UE_Context e: Evict){
			RAM0.removeFile(e);				
		}	
		
		evicted=Evict.size();
		Evict.clear();
		insert(file);
		RAM0.addFile(file);
		return -2;
	}
	
	
		
	public void findEXDWeight(double lastScore,double a, double timeNow, double lastAccess){
		double time = timeNow-lastAccess;	
		//System.out.println(time);
		
		for(UE_Context u: CacheState){
			if(!u.equals(file)){
				u.setProbility(u.getEXDScore()*Math.pow(Math.E, -a*(timeNow-u.getEXDTime()))); 
				//System.out.println(u.getProbility());
			}
		}
		file.incAccessNum();	
		
		if(file.getAccessNum()==1){			
			file.setEXDScore(1);
			file.setEXDTime(timeNow);	
			file.setProbility(1);
		}
																									
		file.setProbility(lastScore*Math.pow(Math.E, -a*time)+1);
		file.setEXDScore(lastScore*Math.pow(Math.E, -a*time)+1);
				
		file.setEXDTime(timeNow);	
	}
	
	
	
	public void findEXDAHPWeight(double lastScore,double a, double timeNow, double lastAccess){
		double time = timeNow-lastAccess;	
		//System.out.println(time);
		
		for(UE_Context u: CacheState){
			if(!u.equals(file)){
				u.setProbility(u.getEXDScore()*Math.pow(Math.E, -a*(timeNow-u.getEXDTime()))); 
				//System.out.println(u.getProbility());
			}
		}
		file.incAccessNum();	
		
		if(file.getAccessNum()==1){			
			file.setEXDScore(1);
			file.setEXDTime(timeNow);	
			file.setProbility(1);
		}
			
		if(method.equals("a")){
			file.setProbility(lastScore*Math.pow(Math.E, -a*time)+getAHPWeight(file));
			file.setEXDScore(lastScore*Math.pow(Math.E, -a*time)+getAHPWeight(file));
		}else if(method.equals("a1")){
			file.setProbility(lastScore*Math.pow(Math.E, -a*time)+getAHPWeight(file)+1);
			file.setEXDScore(lastScore*Math.pow(Math.E, -a*time)+getAHPWeight(file)+1);
		}

		file.setEXDTime(timeNow);	
	}
	
	
	public void findLFUWeight(){
		file.incAccessNum();
		int score=file.getAccessNum()+agingFac;
		if(!CacheState.isEmpty()){
			agingFac= (int) CacheState.get(0).getProbility();
		}
		file.setProbility(score);
	}
	
	
	
	private void insert(UE_Context f){
		CacheState.add(f);
		Collections.sort(CacheState);
	}
	
	private void remove(UE_Context f){
		CacheState.remove(f);
		Collections.sort(CacheState);
	}
		

	public double getAHPWeight(UE_Context file){
		if(file.getCriteria()==0){
			return ahp.getResult()[0];
		}else if(file.getCriteria()==1){
			return ahp.getResult()[1];
		}else if(file.getCriteria()==2){
			return ahp.getResult()[2];
		}else if(file.getCriteria()==3){
			return ahp.getResult()[3];
		}
		return 1;	
	}
	
}
