package CS218Project;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;



public class Testing {

	public static synchronized void main(String[] args) throws ParameterException {
		// TODO Auto-generated method stub
//		testThread t = new testThread();
//		AHP x = new AHP(4);
//
//		x.setWeight(0,1,10);
//		x.setWeight(0,2,10);
//		x.setWeight(0,3,10);
//		x.setWeight(1,2,6);
//		x.setWeight(1,3,10);
//		x.setWeight(2,3,10);
//
//		System.out.println(x.getMX());
//
//		x.findWeight();
//		System.out.println(x);
//				
//		synchronized (t) {
//		    t.notify();
//		}	
		CloudSim.init(1, Calendar.getInstance(), false);
		
		ArrayList< UE_Context> UE = new ArrayList< UE_Context>();
		HarddriveStorage Harddrive = new HarddriveStorage("hardd1",9999999);
		Ratio ratio = new Ratio(1);
		ArrayList <History> records = new 	ArrayList <History>();

		
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
		
//		UE.get(4).setRatio(1);
//		Collections.sort(UE);
//		for( UE_Context x:UE){
//			System.out.println(x);
//		}

		
		CachingAlgorithm caching = new CachingAlgorithm(Harddrive, records, ratio);
		
		Thread t1 = new Thread(caching);
		t1.start();
		
		AdaptorAlgorithm adopt = new AdaptorAlgorithm(10, records, ratio);		
		Thread t2 = new Thread(adopt);
		t2.start();
		

	
		Scanner scan=null;
		while(true){
			scan=new Scanner(System.in);
			String s = scan.next();
			
			try{
				UE_Context u=UE.get(Integer.parseInt(s) );
				caching.addrequest(u);
			    }
		    catch(Exception ex) {
				UE_Context u=UE.get(rn.nextInt(100));
				caching.addrequest(u);
				System.out.println(u.getName()+" requested");
			    }
			
		}
		
	}
	
}
