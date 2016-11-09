package CS218Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;

public class ZEXO_test {
	static int eventNum=0;
	static int eventCap=5;
	static int groupSize=10;
	static   TreeMap<Double, Group> groups = new TreeMap<Double, Group>();
	static Map<Double,valueSet> candidateValue = new  TreeMap<Double,valueSet>();
	static ArrayList<UE_Context> UE = new ArrayList< UE_Context>();		
	static 	HarddriveStorage Harddrive = null;
	static ArrayList <UE_Context> Evict=new ArrayList <UE_Context>();
	static ArrayList<UE_Context> CacheState = new ArrayList<UE_Context>();
	static ArrayList <History> records = new ArrayList<History>();
			
	static double used=0;
	static double capacity=0;
	static double freespace=0;
	static double sumWeight=0;
	static UE_Context file=null;
	static Ratio ratio=new Ratio(Math.pow(10, -4));
	static History history = new History(0,0,0,ratio.getRatio());

	public static void main(String[] args) throws ParameterException {
		Log.disable();
		CloudSim.init(1, Calendar.getInstance(), false);
		Harddrive =  new HarddriveStorage("HD0",20);
		fillhardrive(Harddrive);
		Distribution distr = new Distribution(0.25,1);
		
		file=new UE_Context("d",1,0.5,5,1);
		
		while(true){
		Random oz=new Random(); 		
		UE_Context u=UE.get(oz.nextInt(99));
		double d = Math.random();
		int c = u.getCriteria();
		if(c<2 && d <=.8){
			file = u;
		}else if(c>=2 && d <=.2){
			file = u;
		}
		
	
		int result= handleRequest(distr.nextTime());
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
		System.out.println(history+" "+ratio.getRatio());
		//adopt();
		
		}
		
		
		
		
		
		

	}
	
	public static double findEXOWeight(double weight,double a, double timeNow, double firstAccess){
		double time = timeNow-firstAccess;	
		//System.out.println(time);
		for(UE_Context u: CacheState){
			u.setProbility(weight*Math.pow(Math.E, -a*time)); 
			//System.out.println(u.getRatio());
		}
		file.incAccessNum();	
		if(file.getAccessNum()==1){
			return Math.pow(Math.E, -a*time);
		}
		return weight*Math.pow(Math.E, -a*time)+1;
	}
	
	public static void insert(UE_Context f){
		CacheState.add(f);
		Collections.sort(CacheState);
	}
	
	public static void remove(UE_Context f){
		CacheState.remove(f);
		Collections.sort(CacheState);
	}

	public static int handleRequest(double time){			
		used=Harddrive.getCurrentSize();
		capacity=Harddrive.getCapacity();
		
		Log.printLine("Used:"+used+" Capacity:"+capacity);		
		Log.printLine("time      "+time+"       "+ (time-file.getEXOTime()));
		
		double EXOScore=findEXOWeight(file.getEXOScore(),ratio.getRatio(),time,file.getEXOTime());
		file.setProbility(EXOScore);
		if(file.getAccessNum()==1){
			file.setEXOScore(EXOScore);
			file.setEXOTime(time);
		}
		
		//System.out.println( EXOScore);
	
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
	
	
	
	private static ArrayList<UE_Context> fillhardrive(HarddriveStorage Harddrive) throws ParameterException{
		Random rn = new Random();
		Random oz=new Random(); 
		for(int i=0;i<100;i++){
			int max=3;
			int min=0;
			int ran= rn.nextInt(max - min + 1) + min;
			UE.add(new UE_Context(Integer.toString(i),1,0.5,5,ran));
			
			oz=new Random(); 
			int x=oz.nextInt(1); 
			if(x==1){
				Harddrive.addFile(new UE_Context(Integer.toString(i),1,0.5,5,ran));
			}
		}
		return UE;
	}
}
