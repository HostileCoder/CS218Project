package CS218Project;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;



public class Testing {

	public static synchronized void main(String[] args) {
		// TODO Auto-generated method stub
		testThread t = new testThread();
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
				
		synchronized (t) {
		    t.notify();
		}
	
		 
		TreeMap<Double,valueSet> candidateValue = new  TreeMap<Double,valueSet>();
		candidateValue.put(2.2, new valueSet(2.2));
		candidateValue.put(1.3, new valueSet(1.2));
		candidateValue.put(0.0, new valueSet(0.0));
		
		candidateValue.get(candidateValue.firstKey());
		
	
		System.out.println(candidateValue.get(candidateValue.lastKey()).getValueRatio());
		
		
	}
	



}
