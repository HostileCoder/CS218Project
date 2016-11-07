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



public class ZTesting {

	public static synchronized void main(String[] args) throws ParameterException {
		// TODO Auto-generated method stub

		CloudSim.init(1, Calendar.getInstance(), false);
		
		
		HarddriveStorage Harddrive = new HarddriveStorage("hardd1",30);
		Ratio ratio = new Ratio(1);
		ArrayList <History> records = new 	ArrayList <History>();

		ArrayList< UE_Context> UE = new ArrayList< UE_Context>();
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
		
		
		CachingAlgorithm caching = new CachingAlgorithm(Harddrive, records, ratio);
		
		Thread t1 = new Thread(caching);
		t1.start();
		
		AdaptorAlgorithm adopt = new AdaptorAlgorithm(10, records, ratio);		
		Thread t2 = new Thread(adopt);
		t2.start();
		
		int x=200;
		while(true){
			UE_Context u=UE.get(oz.nextInt(99));
			double d = Math.random();
			int c = u.getCriteria();
			if(c<2 && d <.8){
				caching.addrequest(u);
			}else if(c>=2 && d <.2){
				caching.addrequest(u);
			}
		}
		
		
	
//		Scanner scan=null;
//		while(true){
//			scan=new Scanner(System.in);
//			String s = scan.next();
//			
//			try{
//				UE_Context u=UE.get(Integer.parseInt(s) );
//				caching.addrequest(u);
//			    }
//		    catch(Exception ex) {
//				UE_Context u=UE.get(rn.nextInt(100));
//				caching.addrequest(u);
//				System.out.println(u.getName()+" requested");
//			    }
//			
//		}
		
		
	}
	
}









//ZtestThread t = new ZtestThread();
//synchronized (t) {
//    t.notify();
//}	
