package CS218Project;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
	private Ratio ratio=new Ratio(Math.pow(30, -2));
	private History history = new History(0,0,0,ratio.getRatio());
	private Thread t=null;
	
	private int rowIndex=0;
	private HSSFWorkbook workbook = new HSSFWorkbook();
	private Sheet Sheet = workbook.createSheet("Cache");
	private FileOutputStream output = new FileOutputStream("output.xls");
	private PrintWriter outputWriter = new PrintWriter ("file.txt");
	
	public myDatacenterEXO(String name, DatacenterCharacteristics characteristics, VmAllocationPolicy vmAllocationPolicy,
			List<Storage> storageList, double schedulingInterval) throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);
		// TODO Auto-generated constructor stub	
		Harddrive=(HarddriveStorage) storageList.get(0);	
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
		for(int i=1;i<=6;i++){
			valueSet v=new valueSet(Math.pow(10, -i*2));
			candidateValue.put(Math.pow(10, -i*2), v );			
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
		System.out.println(history+" "+ratio.getRatio());
		 
		outputWriter.write(time+"	"+history.getBHR()+"\n");
		
		//adopt();
		
	}
	

	private int handleRequest(double time){			
		used=Harddrive.getCurrentSize();
		capacity=Harddrive.getCapacity();
		
		Log.printLine("Used:"+used+" Capacity:"+capacity);
		
		Log.printLine("time      "+time+"       "+ (time-file.getEXOTime()));
		
		double EXOScore=findEXOWeight(file.getEXOScore(),ratio.getRatio(),time,file.getEXOTime());
		
		
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
					//System.out.println("Adopt	:"+v.getValueRatio()+" remove from "+g.getName());	
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
	
	
	
	
	public double findEXOWeight(double firtScore,double a, double timeNow, double firstAccess){
		double time = timeNow-firstAccess;	
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
			return Math.pow(Math.E, -a*time);
		}
		//System.out.println(firtScore*Math.pow(Math.E, -a*time)+1);
		file.setProbility(firtScore*Math.pow(Math.E, -a*time)+1);
		return firtScore*Math.pow(Math.E, -a*time)+1;
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
	
	
//	public void writedata(History h){
//		 Row row = Sheet.createRow(rowIndex++);
//		 int cellIndex = 0;
//		 row.createCell(cellIndex++).setCellValue(h.getTotalRequest());
//		 row.createCell(cellIndex++).setCellValue(h.getBHR());
//		 try {
//			workbook.write(output);	
//		 }catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//	}
	
}
