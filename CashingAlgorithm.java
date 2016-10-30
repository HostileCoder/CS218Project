package CS218Project;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.HarddriveStorage;



class CashingAlgorithm implements Runnable{
		
		private HarddriveStorage Harddrive = null;
		private ArrayList <String> Evict=new ArrayList <String>();
		private Map<String, UE_Context> CachedState = new TreeMap<String, UE_Context>();
		private ArrayList <UE_Context> requests = new ArrayList<UE_Context>();
		private ArrayList <History> records = new ArrayList<History>();
		
		private double used=0;
		private double capacity=0;
		private double freespace=0;
		private double sumWeight=0;
		private UE_Context file=null;
		private AHP ahp =new AHP(4);
		private Ratio ratio=null;

		
		public CashingAlgorithm(Map<String, UE_Context> CachedState, HarddriveStorage Harddrive, ArrayList <History> records, Ratio ratio){
			this.CachedState=CachedState;
			this.Harddrive=Harddrive;
			this.records=records;
			this.ratio=ratio;
			ahp.setWeight(0,1,10);
			ahp.setWeight(0,2,10);
			ahp.setWeight(0,3,10);
			ahp.setWeight(1,2,ratio.getRatio());
			ahp.setWeight(1,3,10);
			ahp.setWeight(2,3,10);
			
		}
		
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub	
			double TotalHit = 0.0;
			double NumInsert = 0.0;
			double TotalRequest = 0.0;
				
			while(true)	
			{
				while(!requests.isEmpty())
				{
					
					double r= ratio.getRatio();
					
					ahp.setWeight(1,2,r);									
					
					boolean result= handleRequest();					
					TotalRequest++;
													
					if(result==false){
						TotalHit++;			
					}else{
						TotalRequest++;
					}
					
					records.add(new History(TotalHit, NumInsert, TotalRequest,r));
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			}				
		}
		
		
		public boolean handleRequest(){
			file=requests.remove(0);			
			used=Harddrive.getCurrentSize();
			capacity=Harddrive.getCapacity();
			ahp.findWeight();
			
			if(CachedState.get(file.getName()) != null){
				return false;
			}
			
			if(file.getSize()+used<=capacity){
				updateRatio(file);
				used=used+file.getSize();
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
				return false;
			}
						
			//remove from CachedState
			for(String e: Evict){		
				CachedState.remove(e);								
			}	
			//remove from Cache			
			for(String e: Evict){		
				Harddrive.deleteFile(e);			
			}	
			
			Evict.clear();
			insert(file);
			return true;
		}
		
		
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

	
		public void addrequest(File file){
			 requests.add((UE_Context) file);
		}
		
		public void insert(UE_Context f){
			CachedState.put(f.getName(), f);
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
		

		public ArrayList <String> getEvict() {
			return Evict;
		}

		public void setEvict(ArrayList <String> evict) {
			Evict = evict;
		}

		 
}
	

