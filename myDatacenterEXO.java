package CS218Project;


import java.io.FileOutputStream;
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
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

public class myDatacenterEXO extends Datacenter{
	//------------------------------------------------------------------------adopt
	private int eventNum=0;
	private int eventCap=5;
	private int groupSize=10;
	private TreeMap<Double, Group> groups = new TreeMap<Double, Group>();
	private Map<Double,valueSet> candidateValue = new  TreeMap<Double,valueSet>();
	private double preBHR=0;
	private double preBIR=0;	
	//------------------------------------------------------------------------
	private HarddriveStorage Harddrive = null;
	private ArrayList <UE_Context> Evict=new ArrayList <UE_Context>();
	private ArrayList<UE_Context> CacheState = new ArrayList<UE_Context>();
	private ArrayList <History> records = new ArrayList<History>();
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

	private PrintWriter outputWriter = new PrintWriter ("file.txt");
	public myDatacenterEXO(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy,
			List<Storage> storageList, double schedulingInterval) throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
		// TODO Auto-generated constructor stub	
		Harddrive=(HarddriveStorage) storageList.get(0);	

		
		
		//---------------------------------------------adopt		
		double threshold = 1.0/(double) groupSize;
		double nam=0.0;		
		for(int i=0;i<groupSize;i++){
			Group g= new Group(nam);
			groups.put(nam, g);
			nam=nam+threshold;
			nam=Math.round(nam*10.0)/10.0;
		}


		for(double i=-1;i<=1.2;i=i+0.4){
			double a=Math.pow(10, i);
			a=Math.round(a*100.0);
			a=a/100.0;
//			valueSet v=new valueSet((double) 2*i);
//			candidateValue.put((double) 2*i, v );			
			valueSet v=new valueSet(a);
			candidateValue.put(a, v );	
			group(v);
		}
		
		//---------------------------------------------
		
		ahp.setWeight(0,1,5);
		ahp.setWeight(0,2,5);
		ahp.setWeight(0,3,5);
		ahp.setWeight(1,2,5);
		ahp.setWeight(1,3,10);
		ahp.setWeight(2,3,10);
		ahp.findWeight();
		
	}
	
	
	
	protected void processCloudletSubmit(SimEvent ev, boolean ack) {
		double time = ev.eventTime();
			
		myCloudlet cl = (myCloudlet) ev.getData();
		
		//time=cl.getSubmissionTime();
		//System.out.println(time);
		
		file = cl.getUE();			
		int result= handleRequest(time);
		history.incTotalRequest();
		
		history.incCriteria(file.getCriteria(),result);
				
		if(result==0){
			//Log.printLine("Cache:Missed!");
		}else if(result==1){
			//Log.printLine("Cache:Hit!");
		}else if(result==-1){
			//System.out.println("Cache:Missed!, No insertion");
		}else if(result==-2){
			//System.out.println("Cache:Missed!, with eviction and insertion"+" "+evicted);
		}
		
		
		if(result==1){
			history.incTotalHit();		
		}
		if(result==0||result==-2){       
			history.incNumInsert();	
			history.incTotalMiss();
		}
		if(result==-1){    
			history.incTotalMiss();
		}
		
		History rec=new History(history.getTotalHit(), history.getNumInsert(), history.getTotalRequest(),ratio.getRatio());
		rec.setBHR(history.getBHR());
		rec.setBIR(history.getBIR());					
		rec.setPreBHR(history.getPreBHR());
		rec.setPreBIR(history.getPreBIR());
		records.add(rec);
		System.out.println(time+" "+history+" 	Ratio:"+ratio.getRatio()+
				" 	HP:"+history.HPH/history.HP+
				" 	LP:"+history.LPH/history.LP+
				" 	HB:"+history.HBH/history.HB+
				" 	LB:"+history.LBH/history.LB
				);	 
		

		outputWriter.write(time+" "+history.getTotalRequest()+"	BHR:"+history.getBHR()+"  BIR:"+history.getBIR()+
				" 	"+history.HPH/history.HP+
				" 	"+history.LPH/history.LP+
				" 	"+history.HBH/history.HB+
				" 	"+history.LBH/history.LB+
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
		
		
		//adopt();
		
	}
	

	private int handleRequest(double time){			
		used=Harddrive.getCurrentSize();
		capacity=Harddrive.getCapacity();
		
		//System.out.println("Used:"+used+" Capacity:"+capacity);	---------------------------------------------------------------------------	
		
		findEXOWeight(file.getEXOScore(),ratio.getRatio(),time,file.getEXOTime());
		//findLFUWeight();
		
		if(CacheState.contains(file)){
			return 1;
		}
		
		if(file.getSize()+used<=capacity){
			used=used+file.getSize();
			insert(file);
			Harddrive.addFile(file)	;
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
			return -1;
		}
					
		//remove from CachedState
		for(UE_Context u: Evict){
			remove(u);
			//System.out.println("evict cachedstate..."+ u.getName());											
		}	
		
		//remove from Cache
		for(UE_Context e: Evict){
			Harddrive.deleteFile(e);
			//System.out.println("evict Harddrive..."+ e.getName());
							
		}	
		evicted=evicted+Evict.size();
		Evict.clear();
		insert(file);
		Harddrive.addFile(file)	;
		return -2;
	}
	
	public void adopt(){
		
		History history = records.remove(0);			
		eventNum++;				
						
		valueSet x=null;	
		x=candidateValue.get(history.getVarRatio());
		x.setValueBHR(history.getBHR());
		x.setValueBIR(history.getBIR());
		
		if(eventNum==eventCap){
			//Log.printLine("Adopt	Retrieved"+ x);
			eventNum=0;			
			double h=( history.getBHR() + 2*preBHR )/3; 
			double i=( history.getBIR() + 2*preBIR )/3;
			x.setValueBHR(h);
			x.setValueBIR(i);
			preBHR=h;
		    preBIR=i;
			
			//System.out.println("Adopt	modified"+ x);
			for(Group g: groups.values()){
				if(g.getValueSetList().containsKey(x.getValueRatio())){
					valueSet v = g.getValueSetList().remove(x.getValueRatio());
					g.findRep();
				}
			}			
			group(x);	
			
			//group with largest representative value (BHR)								
			Group selected = null;						
			double max=0.0;
			for(Group g: groups.values()){
				if(g.getRep()>=max){
					max=g.getRep();
					selected = g;
				}
			}

			
			//ratio with smallest BIR in the group
			double newRatio;
			if(selected.valueSetWithSmallestBIR()==null){
				newRatio=ratio.getRatio();
			}else
				newRatio=selected.valueSetWithSmallestBIR().getValueRatio();
				
//			System.out.println("");	
//			System.out.println("Adopt	group selected:"+selected);	
//			System.out.println("Adopt	New Ratio:"+newRatio);
//			System.out.println("Adopt	History inserted: "+history);		
//			System.out.println(" ");			

					
			ratio.setRatio(newRatio);	
			}
	
	}
		
	public void findEXOWeight(double lastScore,double a, double timeNow, double lastAccess){
		double time = timeNow-lastAccess;	
		//System.out.println(time);
		
		for(UE_Context u: CacheState){
			if(!u.equals(file)){
				u.setProbility(u.getEXOScore()*Math.pow(Math.E, -a*(timeNow-u.getEXOTime()))); 
				//System.out.println(u.getProbility());
			}
		}
		file.incAccessNum();	
		
		if(file.getAccessNum()==1){			
			file.setEXOScore(1);
			file.setEXOTime(timeNow);	
			file.setProbility(1);
		}
																				
//		file.setProbility(lastScore*Math.pow(Math.E, -a*time)+1+getAHPWeight(file));
//		file.setEXOScore(lastScore*Math.pow(Math.E, -a*time)+1+getAHPWeight(file));
					
		file.setProbility(lastScore*Math.pow(Math.E, -a*time)+1);
		file.setEXOScore(lastScore*Math.pow(Math.E, -a*time)+1);
		
		
		file.setEXOTime(timeNow);	
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
		
	public void group(valueSet v){
		double x = v.getValueBHR();
		if(x<=0.1){
			Group g = groups.get(0.0);
			g.addvalue(v);
			//System.out.println("insert at group 0.0");
			return;
		}
		
		for(Group g : groups.values()){
			if(x-g.getName()<0.1){
				g.addvalue(v);
				//System.out.println("insert at group "+g.getName());
				return;
			}
		}		
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
