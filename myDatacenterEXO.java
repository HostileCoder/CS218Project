package CS218Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
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
	
	
	private double used=0;
	private double capacity=0;
	private double freespace=0;
	private double sumWeight=0;
	private UE_Context file=null;
	private Ratio ratio=new Ratio(Math.pow(10, -2));
	private History history = new History(0,0,0,ratio.getRatio());
	private Thread t=null;
	
	
	public myDatacenterEXO(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy,
			List<Storage> storageList, double schedulingInterval) throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
		// TODO Auto-generated constructor stub	
		Harddrive=(HarddriveStorage) storageList.get(0);	
		
//		AdaptorAlgorithmEXO adopt = new AdaptorAlgorithmEXO(10, records, ratio);		
//		t = new Thread(adopt);
//		t.start();		
		Log.disable();
		
		
		//---------------------------------------------adopt
		
		double threshold = 1.0/(double) groupSize;
		double nam=0.0;		
		for(int i=0;i<groupSize;i++){
			Group g= new Group(nam);
			groups.put(nam, g);
			nam=nam+threshold;
			nam=Math.round(nam*10.0)/10.0;
		}

		double a=0.0;
		for(int i=0;i<6;i++){
			valueSet v=new valueSet(Math.pow(10, -2*i));
			candidateValue.put( Math.pow(10, -2*i), v );			
//			a=0.5+a;
//			a=Math.round(a*10.0)/10.0;
			group(v);
		}
		
		//---------------------------------------------
		
	}
	
	
	
	protected void processCloudletSubmit(SimEvent ev, boolean ack) {
		double time = ev.eventTime();
		
		myCloudlet cl = (myCloudlet) ev.getData();
		file = cl.getUE();
		
		int result= handleRequest(time);
		history.incTotalRequest();
		
		Log.printLine("return "+result);
				
		if(result==0){
			//Log.printLine("Cache:Missed!");
		}else if(result==1){
			//Log.printLine("Cache:Hit!");
		}else if(result==-1){
			//System.out.println("Cache:Missed!, No insertion");
		}else if(result==-2){
			//System.out.println("Cache:Missed!, with evivtion and insertion");
		}
		
		
		if(result==1){
			history.incTotalHit();		
		}else if(result==0||result==-2){
			history.incNumInsert();		
		}
		
		History rec=new History(history.getTotalHit(), history.getNumInsert(), history.getTotalRequest(),ratio.getRatio());
		rec.setBHR(history.getBHR());
		rec.setBIR(history.getBIR());					
		rec.setPreBHR(history.getPreBHR());
		rec.setPreBIR(history.getPreBIR());
		records.add(rec);
		//System.out.println(history+" "+ratio.getRatio());
		adopt();
		
	}
	

	private int handleRequest(double time){			
		used=Harddrive.getCurrentSize();
		capacity=Harddrive.getCapacity();
		
		Log.printLine("Used:"+used+" Capacity:"+capacity);
		
		Log.printLine("time      "+time+"       "+ (time-file.getEXOTime()));
		
		double EXOScore=findEXOWeight(file.getEXOScore(),ratio.getRatio(),time,file.getEXOTime());
		file.setProbility(EXOScore);
		if(file.getEXOScore()==0)
			file.setEXOScore(EXOScore);
		file.setEXOTime(time);
	
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
			Log.printLine("Cache:Missed!, No insertion");
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
					System.out.println("Adopt	:"+v.getValueRatio()+" remove from "+g.getName());	
				}
			}			
			group(x);	
			
			//group with largest representative value (BHR)								
			Group selected = null;						
			double max=0.0;
			for(Group g: groups.values()){
				//System.out.println(g);
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
				
			System.out.println("");	
			System.out.println("Adopt	group selected:"+selected);	
			System.out.println("Adopt	New Ratio:"+newRatio);
			System.out.println("Adopt	History inserted: "+history);		
			System.out.println(" ");			
//			System.out.println("Adopt	Selected Group Members ");
//			for(valueSet v:selected.getValueSetList().values()){
//				System.out.println("     	"+v);
//			}
					
			ratio.setRatio(newRatio);	
			}
	}
	
	
	
	
	public double findEXOWeight(double weight,double a, double timeNow, double lastAccess){
		file.incAccessNum();
		double time = timeNow-lastAccess;		
		if(weight==0){
			return Math.pow(Math.E, -a*time);
		}
		if(file.getAccessNum()==2){
			return weight*Math.pow(Math.E, -a*time)+1;
		}
		return weight*Math.pow(Math.E, -a*time);
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
			Log.printLine("insert at group 0.0");
			return;
		}
		
		for(Group g : groups.values()){
			if(x-g.getName()<0.1){
				g.addvalue(v);
				Log.printLine("insert at group "+g.getName());
				return;
			}
		}
		
	}
}
