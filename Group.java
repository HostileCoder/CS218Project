package CS218Project;

import java.util.ArrayList;

public class Group {
	private double RBHR=0;
	private ArrayList<Member> ms=new ArrayList<Member>();
	public double low=0;
	public double high=0;
	
	public Group(double low , double high){
		this.low=low;
		this.high=high;
	}
	
	public void addMemeber(Member m){
		ms.add(m);
		findRBHR();
	}

	public double findRBHR(){
		if(ms.size()==0){
			return 0;
		}
		double a=0;
		for(Member m:ms){
			a=a+m.getBHR();
		}	
		a=a/(double)ms.size();
		return a;
	}

		
}
