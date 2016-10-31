package CS218Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Log;



class CachingAlgorithm implements Runnable{
		
		private HarddriveStorage Harddrive = null;
		private ArrayList <UE_Context> Evict=new ArrayList <UE_Context>();
		//private Map<String, UE_Context> CacheState = new TreeMap<String, UE_Context>();
		private ArrayList<UE_Context> CacheState = new ArrayList<UE_Context>();
		private ArrayList <UE_Context> requests = new ArrayList<UE_Context>();
		private ArrayList <History> records = new ArrayList<History>();
		private History history = null;
		
		private double used=0;
		private double capacity=0;
		private double freespace=0;
		private double sumWeight=0;
		private UE_Context file=null;
		private AHP ahp =new AHP(4);
		private Ratio ratio=null;

		
		public CachingAlgorithm(HarddriveStorage Harddrive, ArrayList <History> records, Ratio ratio){
			this.Harddrive=Harddrive;
			this.records=records;
			this.ratio=ratio;
			ahp.setWeight(0,1,10);
			ahp.setWeight(0,2,10);
			ahp.setWeight(0,3,10);
			ahp.setWeight(1,2,ratio.getRatio());
			ahp.setWeight(1,3,10);
			ahp.setWeight(2,3,10);
			history = new History(0,0,0,ratio.getRatio());
		}
		
		@Override
		public void run() 
		{
	
//			double TotalHit = 0.0;
//			double NumInsert = 0.0;
//			double TotalRequest = 0.0;
				
			while(true)	
			{

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				while(!requests.isEmpty())
				{
					
					double r= ratio.getRatio();	
					Log.printLine("The current ratio is: "+r);
					
					setWeights(r);	
					ahp.findWeight();
					
					Log.printLine("Weight Vector is:");
					Log.printLine(ahp.getResult()[0]+" "+ahp.getResult()[1]+" "+ahp.getResult()[2]+" "+ahp.getResult()[3]);
					
					int result= handleRequest();
					history.incTotalRequest();
					
					Log.printLine("return "+result);
						//TotalRequest++;													
					if(result==1){
						history.incTotalHit();
						//TotalHit++;			
					}else if(result==0){
						history.incNumInsert();
						//NumInsert++;
						
					}
					
					History rec=new History(history.getTotalHit(), history.getNumInsert(), history.getTotalRequest(),r);
					rec.setBHR(history.getBHR());
					rec.setBIR(history.getBIR());					
					rec.setPreBHR(history.getPreBHR());
					rec.setPreBIR(history.getPreBIR());
					records.add(rec);
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
				}
			}				
		}
		
		
		public int handleRequest(){
			file=requests.remove(0);			
			used=Harddrive.getCurrentSize();
			capacity=Harddrive.getCapacity();
			
			Log.printLine("Used:"+used+" Capacity:"+capacity);
			
		
			updateRatio(file);
		
			if(CacheState.contains(file)){
				Log.printLine("Cache:Hit!");
				return 1;
			}
			
			if(file.getSize()+used<=capacity){
				Log.printLine("Cache:Missed!");
				//updateRatio(file);
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
					 if(freespace>file.getSize()){
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
			Log.printLine("Cache:Missed!");
			return 0;
		}
		
		
		public void updateRatio(UE_Context file){
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

	
		public void addrequest(File file){
			 requests.add((UE_Context) file);
		}
		
		public ArrayList <UE_Context> getrequest(){
			return  requests;
		}
		
//		public void insert(UE_Context f){
//			CacheState.put(f.getName(), f);
//		}
		
		public void insert(UE_Context f){
			CacheState.add(f);
			Collections.sort(CacheState);
		}
		
		public void remove(UE_Context f){
			CacheState.remove(f);
			Collections.sort(CacheState);
		}
		
		public double getUsed() {
			return used;
		}

		public void setUsed(int used) {
			this.used = used;
		}

		public double getCapacity() {
			return capacity;
		}

		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}

		public AHP getAhp() {
			return ahp;
		}
		
		
		public ArrayList<UE_Context> getCacheState(){
			return CacheState;
		}


		public void setWeights(double r){
			ahp.setWeight(0,1,10);
			ahp.setWeight(0,2,10);
			ahp.setWeight(0,3,10);
			ahp.setWeight(1,2,r);
			ahp.setWeight(1,3,10);
			ahp.setWeight(2,3,10);
		}
		 
}
	

