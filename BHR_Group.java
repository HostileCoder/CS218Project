package CS218Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BHR_Group {
	private valueSet smallestBIR=null;
	private valueSet largestBHR=null;
	
	private double rep=0;
	private String name="";
	private Map<Integer,Double> pair =new HashMap<Integer,Double>();
	private ArrayList<valueSet> valueSetList = new ArrayList<valueSet>();
	
	public BHR_Group(String name){
		this.name=name;
	}
	
	
	public void add(valueSet v){
		if(smallestBIR==null ){
			valueSetList.add(v);
			smallestBIR=v;
		
		}		
		
		else if(v.getBIR()<smallestBIR.getBIR()){
			smallestBIR=v;
			valueSetList.add(v);
		}		
		
	}
	
	public void clearList(){
		 valueSetList.clear();
		 smallestBIR=null; 
	}
	
	
	public double findRep(){
		double sum=0.0;
		for(valueSet e: valueSetList ){
			sum=sum+e.getBHR();
		}	
		return rep=sum/(double)valueSetList.size();
	}

	
//	public void add(int x, double y){
//		pair.put(x, y);
//		rep=findRep();
//	}
//	
//	public double getMax(){
//		return Collections.max( pair.values());
//	}
//	
//	public Map<Integer,Double> getPair() {
//		return pair;
//	}
//	public void setPair(Map<Integer,Double> pair) {
//		this.pair = pair;
//	}
	public double getRep() {
		return rep;
	}


	public valueSet getSmallestBIR() {
		return smallestBIR;
	}


	public void setSmallestBIR(valueSet smallestBIR) {
		this.smallestBIR = smallestBIR;
	}


	
	
}
