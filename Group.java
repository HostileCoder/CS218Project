package CS218Project;


import java.util.HashMap;
import java.util.Map;

public class Group {
	private valueSet smallestBIR=null;

	
	private double rep=0;
	private String name="";
	private Map<Double,valueSet> valueSetList = new  HashMap<Double,valueSet>();
	
	public Group(String name){
		this.setName(name);
	}
	
	
	public void addvalue(valueSet v){
		if(smallestBIR==null ){
			valueSetList.put(v.getValueRatio(),v);
			smallestBIR=v;
		
		}		
		
		else if(v.getValueBIR()<smallestBIR.getValueBIR()){
			smallestBIR=v;
			valueSetList.put(v.getValueRatio(),v);
		}		
		
	}
	
	public void clearList(){
		 valueSetList.clear();
		 smallestBIR=null; 
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
	
}
