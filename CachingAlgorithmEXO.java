package CS218Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Log;



class CachingAlgorithmEXO implements Runnable{
		
		private HarddriveStorage Harddrive = null;
		private ArrayList <UE_Context> Evict=new ArrayList <UE_Context>();
		private ArrayList<UE_Context> CacheState = new ArrayList<UE_Context>();
		private ArrayList <UE_Context> requests = new ArrayList<UE_Context>();
		private ArrayList <History> records = new ArrayList<History>();
		private History history = null;
		
		private double used=0;
		private double capacity=0;
		private double freespace=0;
		private double sumWeight=0;
		private UE_Context file=null;
		private Ratio ratio=null;
		private double EXOScore =0;
		private double EXOTime =0;
		private double preEXOTime=0;
		
		public CachingAlgorithmEXO(HarddriveStorage Harddrive, ArrayList <History> records, Ratio ratio){
			this.Harddrive=Harddrive;
			this.records=records;
			this.ratio=ratio;
			history = new History(0,0,0,ratio.getRatio());
		}
		
		@Override
		public void run() 
		{
			while(true)	
			{

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				while(!requests.isEmpty())
				{
					int result= handleRequest();
					history.incTotalRequest();					
					//Log.printLine("return "+result);
													
					if(result==1){
						history.incTotalHit();	
					}else if(result==0){
						history.incNumInsert();					
					}
					
					History rec=new History(history.getTotalHit(), history.getNumInsert(), history.getTotalRequest(),ratio.getRatio());
					rec.setBHR(history.getBHR());
					rec.setBIR(history.getBIR());					
					rec.setPreBHR(history.getPreBHR());
					rec.setPreBIR(history.getPreBIR());
					records.add(rec);
					Log.printLine("Cache	"+history.getBHR());
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
				}
			}				
		}
		
		
		public int handleRequest(){
			double ev=0;
			
			file=requests.remove(0);			
			used=Harddrive.getCurrentSize();
			capacity=Harddrive.getCapacity();
			
			//Log.printLine("Used:"+used+" Capacity:"+capacity);
			
		
			EXOScore=findEXOWeight(file.getEXOScore(),ratio.getRatio(),ev-file.getEXOTime());
			file.setWeight(EXOScore);
			file.setEXOScore(EXOScore);
			file.setEXOTime(ev);
		
			if(CacheState.contains(file)){
				//Log.printLine("Cache:Hit!");
				return 1;
			}
			
			if(file.getSize()+used<=capacity){
				//Log.printLine("Cache:Missed!");
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
					 //Log.printLine(x.getName()+" chosen for eviction");
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
			Log.printLine("Cache:Missed!");
			return 0;
		}
		

		public void addrequest(File file){
			 requests.add((UE_Context) file);
		}
		
		public ArrayList <UE_Context> getrequest(){
			return  requests;
		}
		
		
		public void insert(UE_Context f){
			CacheState.add(f);
			Collections.sort(CacheState);
		}
		
		public void remove(UE_Context f){
			CacheState.remove(f);
			Collections.sort(CacheState);
		}
		
		
		public ArrayList<UE_Context> getCacheState(){
			return CacheState;
		}

		
		public double findEXOWeight(double weight,double a, double time){
			return weight*Math.pow(Math.E, -a*time);
		}
		 
}
	

