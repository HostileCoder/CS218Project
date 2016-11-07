package CS218Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;

public class ZCacheTest {
	public static synchronized void main(String[] args) throws ParameterException {
		CloudSim.init(1, Calendar.getInstance(), false);
		
		ArrayList< UE_Context> UE = new ArrayList< UE_Context>();
		HarddriveStorage Harddrive = new HarddriveStorage("hardd1",5);
		Ratio ratio = new Ratio(75);
		ArrayList <History> records = new 	ArrayList <History>();
		AHP ahp =new AHP(4);
		ahp.setWeight(0,1,10);
		ahp.setWeight(0,2,10);
		ahp.setWeight(0,3,10);
		ahp.setWeight(1,2,ratio.getRatio());
		ahp.setWeight(1,3,10);
		ahp.setWeight(2,3,10);
		ahp.findWeight();
		//Log.printLine(ahp.getMX());
		
//		for(int i=0;i<ahp.getResult().length;i++)
//			System.out.println(ahp.getResult()[i]);
		
		UE.add(new UE_Context(Integer.toString(0),1,0.5,5,1));
		UE.add(new UE_Context(Integer.toString(1),1,0.5,5,0));
		UE.add(new UE_Context(Integer.toString(2),1,0.5,5,3));
		UE.add(new UE_Context(Integer.toString(3),1,0.5,5,3));
		UE.add(new UE_Context(Integer.toString(4),1,0.5,5,2));
		UE.add(new UE_Context(Integer.toString(5),1,0.5,5,0));
		UE.add(new UE_Context(Integer.toString(6),1,0.5,5,3));
		UE.add(new UE_Context(Integer.toString(7),1,0.5,5,2));
		UE.add(new UE_Context(Integer.toString(8),1,0.5,5,1));
		UE.add(new UE_Context(Integer.toString(9),1,0.5,5,1));	
		
		
		
		for(UE_Context u:UE){
				double [] d=ahp.getResult();
				if(u.getCriteria()==0){
					u.setProbility(d[0]);
				}else if(u.getCriteria()==1){
					u.setProbility(d[1]);
				}else if(u.getCriteria()==2){
					u.setProbility(d[2]);
				}else if(u.getCriteria()==3){
					u.setProbility(d[3]);
				}
				
				//System.out.println(u.getRatio());
			
		}

		
		Harddrive.addFile(UE.get(0));
		Harddrive.addFile(UE.get(1));
		Harddrive.addFile(UE.get(2));
		Harddrive.addFile(UE.get(3));
		Harddrive.addFile(UE.get(4));
		
		


		
		CachingAlgorithm caching = new CachingAlgorithm(Harddrive, records, ratio);
				Thread t1 = new Thread(caching);
		t1.start();
		
		caching.insert(UE.get(0));
		caching.insert(UE.get(1));
		caching.insert(UE.get(2));
		caching.insert(UE.get(3));
		caching.insert(UE.get(4));
		
		for(UE_Context u:caching.getCacheState()){
			System.out.println(u.getRatio());
		}
		
		Scanner scan=null;
		while(true){
			scan=new Scanner(System.in);
			String s = scan.next();
			
			try{
				UE_Context u=UE.get(Integer.parseInt(s) );
				caching.addrequest(u);
				Log.printLine("Try to insert");
				Log.printLine(u.getName()+" "+u.getRatio()+" "+u.getProbility());
				Log.printLine("");
			    }
		    catch(Exception ex) {
		    	if(s.matches("[r][0-9]")){
		    		s=s.substring(s.length() - 1);
		    		ratio.setRatio(Integer.parseInt(s));
		    		System.out.println("change ratio to:"+Integer.parseInt(s));
		    	}else if(s.matches("[l]")){
		    		for(UE_Context u: caching. getCacheState()){
		    			System.out.println(u.getName()+" "+u.getRatio()+" "+u.getProbility());
		    		}
		    	}
//				UE_Context u=UE.get(rn.nextInt(100));
//				caching.addrequest(u);
//				System.out.println(u.getName()+" requested");
			    }
			
		}
		
		
	}
}
