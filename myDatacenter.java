package CS218Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.SimEvent;

public class myDatacenter extends Datacenter{
	
	private ArrayList<UE_Context> UE = new ArrayList< UE_Context>();

	private CachingAlgorithm caching=null;
	private AdaptorAlgorithm adopt =null;
	
	private HarddriveStorage Harddrive = null;
	private ArrayList <UE_Context> Evict=new ArrayList <UE_Context>();
	private ArrayList<UE_Context> CacheState = new ArrayList<UE_Context>();
	private ArrayList <UE_Context> requests = new ArrayList<UE_Context>();
	private ArrayList <History> records = new ArrayList<History>();
	
	
	private double used=0;
	private double capacity=0;
	private double freespace=0;
	private double sumWeight=0;
	private UE_Context file=null;
	private AHP ahp =new AHP(4);
	private Ratio ratio=new Ratio(1);
	private History history = new History(0,0,0,ratio.getRatio());
	
	
	
	public myDatacenter(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy,
			List<Storage> storageList, double schedulingInterval) throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
		// TODO Auto-generated constructor stub
		
		Harddrive=(HarddriveStorage) storageList.get(0);
		ahp.setWeight(0,1,10);
		ahp.setWeight(0,2,10);
		ahp.setWeight(0,3,10);
		ahp.setWeight(1,2,ratio.getRatio());
		ahp.setWeight(1,3,10);
		ahp.setWeight(2,3,10);
	
		AdaptorAlgorithm adopt = new AdaptorAlgorithm(10, records, ratio);		
		Thread t2 = new Thread(adopt);
		t2.start();
	}
	
	
	
	protected void processCloudletSubmit(SimEvent ev, boolean ack) {
		
		myCloudlet cl = (myCloudlet) ev.getData();
		file = cl.getUE();

		
		double r= ratio.getRatio();	
		Log.printLine("The current ratio is: "+r);
		
		setWeights(r);	
		ahp.findWeight();
		
		Log.printLine("Weight Vector is:");
		Log.printLine(ahp.getResult()[0]+" "+ahp.getResult()[1]+" "+ahp.getResult()[2]+" "+ahp.getResult()[3]);
		
		int result= handleRequest();
		history.incTotalRequest();
		
		Log.printLine("return "+result);
		
		if(result==0){
			Log.printLine("Cache:Missed!");
		}else if(result==1){
			Log.printLine("Cache:Hit!");
		}else if(result==-1){
			Log.printLine("Cache:Missed!, No insertion");
		}else if(result==-2){
			Log.printLine("Cache:Missed!, with evivtion and insertion");
		}
		
		
		if(result==1){
			history.incTotalHit();		
		}else if(result==0||result==-2){
			history.incNumInsert();		
		}
		
		History rec=new History(history.getTotalHit(), history.getNumInsert(), history.getTotalRequest(),r);
		rec.setBHR(history.getBHR());
		rec.setBIR(history.getBIR());					
		rec.setPreBHR(history.getPreBHR());
		rec.setPreBIR(history.getPreBIR());
		records.add(rec);
		Log.printLine("Cache	"+history);
		
	}
	

	private int handleRequest(){			
		used=Harddrive.getCurrentSize();
		capacity=Harddrive.getCapacity();
		
		Log.printLine("Used:"+used+" Capacity:"+capacity);
		
	
		updateRatio(file);
	
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
				 Log.printLine(x.getName()+" chosen for eviction");
				 if(freespace>=file.getSize()){
					 break;
				 }						
			}
		}
							
		if(freespace<file.getSize()){
			return -1;
		}
					
		//remove from CachedState
		for(UE_Context u: Evict){
			remove(u);
			Log.printLine("evict cachedstate..."+ u.getName());
											
		}	
		//remove from Cache
		for(UE_Context e: Evict){
			Harddrive.deleteFile(e);
			Log.printLine("evict Harddrive..."+ e.getName());
							
		}	
		
		Evict.clear();
		insert(file);
		Harddrive.addFile(file)	;
		return -2;
	}
	
	
	private  void updateRatio(UE_Context file){
		double [] d=ahp.getResult();
		if(file.getCriteria()==0){
			file.setProbility(d[0]);
		}else if(file.getCriteria()==1){
			file.setProbility(d[1]);
		}else if(file.getCriteria()==2){
			file.setProbility(d[2]);
		}else if(file.getCriteria()==3){
			file.setProbility(d[3]);
		}
		Collections.sort(CacheState);
	}
	
	private void setWeights(double r){
		ahp.setWeight(0,1,10);
		ahp.setWeight(0,2,10);
		ahp.setWeight(0,3,10);
		ahp.setWeight(1,2,r);
		ahp.setWeight(1,3,10);
		ahp.setWeight(2,3,10);
	}
	
	private void insert(UE_Context f){
		CacheState.add(f);
		Collections.sort(CacheState);
	}
	
	private void remove(UE_Context f){
		CacheState.remove(f);
		Collections.sort(CacheState);
	}
}
