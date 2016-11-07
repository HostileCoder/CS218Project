package CS218Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;

public class ZAdopTest {
	public static synchronized void main(String[] args) throws ParameterException 
	{
		CloudSim.init(1, Calendar.getInstance(), false);
		Ratio ratio = new Ratio(1);
		ArrayList <History> records = new 	ArrayList <History>();
		
		
		
		records.add(new History(4,3,10,ratio.getRatio()));
		records.get(0).setBHR(0.4);
		records.get(0).setBIR(0.3);
		records.get(0).setPreBHR(0.33);
		records.get(0).setPreBIR(0.33);
		
		records.add(new History(5,3,11,ratio.getRatio()));
		records.get(1).setBHR(0.45);
		records.get(1).setBIR(0.27);
		records.get(1).setPreBHR(0.4);
		records.get(1).setPreBIR(0.3);
				
		records.add(new History(6,3,12,ratio.getRatio()));
		records.get(2).setBHR(0.5);
		records.get(2).setBIR(0.25);
		records.get(2).setPreBHR(0.45);
		records.get(2).setPreBIR(0.27);
		
		records.add(new History(6,4,13,ratio.getRatio()));
		records.get(3).setBHR(0.46);
		records.get(3).setBIR(0.31);
		records.get(3).setPreBHR(0.5);
		records.get(3).setPreBIR(0.25);
		
		
		records.add(new History(6,5,14,ratio.getRatio()));
		records.get(4).setBHR(0.43);
		records.get(4).setBIR(0.35);
		records.get(4).setPreBHR(0.46);
		records.get(4).setPreBIR(0.31);
		
		
		
				
		AdaptorAlgorithm adopt = new AdaptorAlgorithm(10, records, ratio);
		Thread t1 = new Thread(adopt);
		
		Group g=adopt.getGroups().get(.1);
		g.setRep(45);
		g.addvalue(new valueSet(1,9,3));
		g.addvalue(new valueSet(4,9,1));
		g.addvalue(new valueSet(2,9,2));
		
		
		
		t1.start();
		
		
//		for(History h: records){
//			adopt.
//		}

//		Scanner scan=null;
//		while(true){
//			scan=new Scanner(System.in);
//			String s = scan.next();
//			
//			try{
//			
//				Integer.parseInt(s);
//				adopt.getRecords();
//				Log.printLine("Try to insert");
//				Log.printLine();
//				Log.printLine("");
//
//			    }
//		    catch(Exception ex) {
//		    	if(s.matches("[r][0-9]")){
//		    		
//		    	}else if(s.matches("[l]")){
//		    		}
//		    	}
//
//		}
			
	}
		
}
