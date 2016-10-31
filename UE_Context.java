package CS218Project;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.ParameterException;

public class UE_Context extends File implements Comparable<UE_Context>{
	private double probility;
	private double weight;
	private double ratio;
	private double missCost;
	private int criteria;
	
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
	
		
	public double findweight(){
		return getMissCost()* getProbility();
	}
	
	public double findratio(){
		return findweight()/getSize();
	}
	

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

}
