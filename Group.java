package CS218Project;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Group {
	private valueSet smallestBIR = null;

	
	private double rep=0.0;
	private String name="";
	private Map<Double,valueSet> valueSetList = new  TreeMap<Double,valueSet>();
	
	public Group(String name){
		this.setName(name);
		//findRep();
	}
	
	
	public void addvalue(valueSet v){
//		if(smallestBIR==null ){
//			valueSetList.put(v.getValueRatio(),v);
//			smallestBIR=v;	
//		}else if(v.getValueBIR()<=smallestBIR.getValueBIR()){
//			smallestBIR=v;
//			valueSetList.put(v.getValueRatio(),v);
//		}
		valueSetList.put(v.getValueRatio(),v);
		findRep();
	}
	
	public void clearList(){
		 valueSetList.clear();
		 smallestBIR=null; 
		 rep=0;
	}
	
	
	public double findRep(){
		double sum=0.0;
		for(valueSet e: valueSetList.values() ){
			sum=sum+e.getValueBHR();
		}	
		rep=sum/(double)valueSetList.size();
		return rep;
	}

	

	public double getRep() {
		return rep;
	}

	public void setRep(double rep) {
		this.rep=rep;
	}

	public valueSet getSmallestBIR() {
		return smallestBIR;
	}


	public void setSmallestBIR(valueSet smallestBIR) {
		this.smallestBIR = smallestBIR;
	}


	public Map<Double,valueSet> getValueSetList() {
		return valueSetList;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return " GroupName:"+getName()+" RepBHR:"+getRep();	
	}
	
	public valueSet valueSetWithSmallestBIR(){
		ArrayList<valueSet> x = new ArrayList<valueSet>();
		
		for(valueSet v:valueSetList.values()){
			x.add(v);
		}
		
		if(x.size()==0){
			return null;
		}
		
		Collections.sort(x, valueSet.compareByBIR);
		return x.get(x.size()-1);
	}
	
	
	public static Comparator<Group> compareByRep
		= new Comparator<Group>() {		
		public int compare(Group v1, Group v2) {
			if(v1.getRep()==v2.getRep()){
				return 0;
			}else if(v1.getRep()>v2.getRep()){
				return -1;
			}
				return 1;
		}
	};
	
	
	}
