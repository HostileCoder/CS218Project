package CS218Project;

import java.util.ArrayList;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.ParameterException;

public class UE_Context extends File implements Comparable<UE_Context>{
	private double probility=0.0;
	private double weight=0.0;
	private double ratio=0.0;
	private double missCost=0.0;
	private int criteria;
	private double EXDTime = 0.0;
	private double EXDScore = 0.0;
	private int accessNum=0;
	
	public UE_Context(String fileName, int fileSize) throws ParameterException {
		super(fileName, fileSize);
		// TODO Auto-generated constructor stub
	}
	
	
	public UE_Context(String fileName, int fileSize, double probility, double missCost, int criteria) throws ParameterException {
		super(fileName, fileSize);
		// TODO Auto-generated constructor stub
		this.probility=probility;
		this.missCost=missCost;
		this.criteria=criteria;
		this.weight = missCost*probility;
		this.ratio = weight/getSize();
	}	

	@Override
	public int compareTo(UE_Context o) {
		// TODO Auto-generated method stub
		if (o.getRatio() < this.getRatio()){
			return 1;
		}else if (o.getRatio() > this.getRatio()){
			return -1;
		}
		return 0;
	}
	
		
//	public double findweight(){
//		return getMissCost()* getProbility();
//	}
//	
//	public double findratio(){
//		return findweight()/getSize();
//	}
	

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getProbility() {
		return probility;
	}

	public void setProbility(double probility) {
		this.probility = probility;
		this.weight = missCost*probility;
		this.ratio = weight/getSize();
	}


	public int getCriteria() {
		return criteria;
	}

	public void setCriteria(int criteria) {
		this.criteria = criteria;
	}

	public double getMissCost() {
		return missCost;
	}

	public void setMissCost(double missCost) {
		this.missCost = missCost;
	}
	
	public int hashCode(){
		return this.getName().hashCode();
	}
	
	public boolean equals(Object o) {
		UE_Context m = (UE_Context) o;
		return this.getName().equals(m.getName());
	}
	
	public String toString(){
		return " "+ratio;
	}


	public double getEXDTime() {
		return EXDTime;
	}


	public double getEXDScore() {
		return EXDScore;
	}

	public void setEXDTime(double EXDTime) {
		this.EXDTime=EXDTime;
	}


	public void setEXDScore(double EXDScore) {
		this.EXDScore=EXDScore;
	}


	public int getAccessNum() {
		return accessNum;
	}


	public void incAccessNum() {
		this.accessNum = accessNum+1;
	}

}
