package CS218Project;

import java.util.Comparator;

public class valueSet {
	private double ratio;
	private double BHR=0.5;
	private double BIR=0.5;
	
	valueSet(double ratio){
		this.ratio=ratio;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getBHR() {
		return BHR;
	}

	public void setBHR(double bHR) {
		BHR = bHR;
	}

	public double getBIR() {
		return BIR;
	}

	public void setBIR(double bIR) {
		BIR = bIR;
	}
	
	
	
	public static Comparator<valueSet> compareByBHR
    	= new Comparator<valueSet>() {		
    		public int compare(valueSet v1, valueSet v2) {
    			if(v1.getBHR()==v2.getBHR()){
    				return 0;
    			}else if(v1.getBHR()>v2.getBHR()){
    				return -1;
    			}
    				return 1;
    		}

    	};
    	
    public static Comparator<valueSet> compareByBIR
    	= new Comparator<valueSet>() {		
    		public int compare(valueSet v1, valueSet v2) {
    			if(v1.getBIR()==v2.getBIR()){
    				return 0;
    			}else if(v1.getBIR()>v2.getBIR()){
    				return -1;
    			}
    				return 1;
    		}

    	};
	

}
