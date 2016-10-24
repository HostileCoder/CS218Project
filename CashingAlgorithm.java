package CS218Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Host;



class CashingAlgorithm implements Runnable{
		//private List <File> fileList=new ArrayList <File>();
		private HarddriveStorage h = null;
		private ArrayList <String> Evict=new ArrayList <String>();
		Map<String, UE_Context> CachedState = new TreeMap<String, UE_Context>();
		
		private History history=null;
		private double used=0;
		private double capacity=0;
		private boolean hit=true;
		private UE_Context file=null;
		private AHP ahp =new AHP(4);
		//private double varRatio=1.0;
		private double freespace=0;
		private double sumWeight=0;
		private boolean hasRequest=false;
		
		public CashingAlgorithm(Map<String, UE_Context> CachedState, HarddriveStorage h, History history ){
			this.CachedState=CachedState;
			this.h=h;
			this.history=history;
			
			ahp.setWeight(0,1,10);
			ahp.setWeight(0,2,10);
			ahp.setWeight(0,3,10);
			ahp.setWeight(1,2,history.getVarRatio());
			ahp.setWeight(1,3,10);
			ahp.setWeight(2,3,10);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub	
			while(true){
				if(hasRequest)
				 handleRequest();
			}
						
		}
		
		
		public boolean handleRequest(){
			history.incTotalRequest();
			
			used=h.getCurrentSize();
			capacity=h.getCapacity();
			ahp.setWeight(1,2,history.getVarRatio());
			ahp.findWeight();
			
			if(CachedState.get(file.getName()) != null){
				history.incTotalHit();
				return false;
			}
			
			if(file.getSize()+used<=capacity){
				history.incNumInsert();
				updateRatio(file);
				insert(file);
				return true;
			}
						
			sumWeight=0;
			freespace=capacity-used;
						
			for(Entry<String, UE_Context> entry : CachedState.entrySet()) {
				String key = entry.getKey();
				UE_Context x= entry.getValue();

				if(sumWeight+x.findweight()<file.getWeight()){
					 sumWeight = sumWeight + x.getWeight();
					 freespace = freespace + x.getSize();
					 Evict.add(key);
					 
					 if(freespace>file.getSize()){
						 break;
					 }
						
				}
			}
								
			if(freespace<file.getSize()){
				history.incTotalHit();
				return false;
			}
						
			//remove from CachedState
			for(String e: Evict){		
				CachedState.remove(e);								
			}	
			//remove from Cache			
			for(String e: Evict){		
				h.deleteFile(e);			
			}	
			
			history.incNumInsert();
			insert(file);
			return true;
		}
		
		
		
		
		
		
		public void insert(UE_Context f){
			CachedState.put(f.getName(), f);
			//Collections.sort(CachedState);
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

//		public double getVarRatio() {
//			return varRatio;
//		}
//
//		public void setVarRatio(double varRatio) {
//			this.varRatio = varRatio;
//		}
		
		public void updateRatio(UE_Context file){
			double [] d=ahp.getResult();
			if(file.getCriteria().equals("00")){
				file.setProbility(d[0]);
			}else if(file.getCriteria().equals("01")){
				file.setProbility(d[1]);
			}else if(file.getCriteria().equals("10")){
				file.setProbility(d[2]);
			}else if(file.getCriteria().equals("11")){
				file.setProbility(d[3]);
			}
		}

		public ArrayList <String> getEvict() {
			return Evict;
		}

		public void setEvict(ArrayList <String> evict) {
			Evict = evict;
		}

		 
}
	

