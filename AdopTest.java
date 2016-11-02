package CS218Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;

public class AdopTest {
	public static synchronized void main(String[] args) throws ParameterException 
	{
		CloudSim.init(1, Calendar.getInstance(), false);
		Ratio ratio = new Ratio(1);
		ArrayList <History> records = new 	ArrayList <History>();
		
		
		
		records.add(new History(30,20,100,ratio.getRatio()));
		
		
				
		AdaptorAlgorithm adopt = new AdaptorAlgorithm(10, records, ratio);
		Thread t1 = new Thread(adopt);
		t1.start();
		


		Scanner scan=null;
		while(true){
			scan=new Scanner(System.in);
			String s = scan.next();
			
			try{
			
				Integer.parseInt(s);
				adopt.getRecords();
				Log.printLine("Try to insert");
				Log.printLine();
				Log.printLine("");

			    }
		    catch(Exception ex) {
		    	if(s.matches("[r][0-9]")){
		    		
		    	}else if(s.matches("[l]")){
		    		}
		    	}

		}
			
	}
		
}
