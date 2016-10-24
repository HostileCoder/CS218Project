package CS218Project;

import java.awt.Point;
import java.util.ArrayList;



public class Testing {

	public static synchronized void main(String[] args) {
		// TODO Auto-generated method stub
		//testThread t = new testThread();
		AHP x = new AHP(4);

		x.setWeight(0,1,10);
		x.setWeight(0,2,10);
		x.setWeight(0,3,10);
		x.setWeight(1,2,6);
		x.setWeight(1,3,10);
		x.setWeight(2,3,10);

		System.out.println(x.getMX());

		x.findWeight();
		System.out.println(x);
		
//		AdopterAlgorithm a=new AdopterAlgorithm(10);
//	
//		for (double value : a.getGroups().keySet()) {
//		    System.out.println(value);
//		}
		
//		synchronized (t) {
//		    t.notify();
//		}
//	
		 
	}
	



}
