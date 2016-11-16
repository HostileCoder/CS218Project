package CS218Project;

import java.util.Comparator;

public class valueSet {
	private double ratio;
	private double BHR=0;
	private double BIR=0;
	
	valueSet(double ratio){
		this.ratio=ratio;
	}

	valueSet(double ratio,double BHR,double BIR){
		this.ratio=ratio;
		this.BHR=BHR;
		this.BIR=BIR;
	}
	
	public double getValueRatio() {
		return ratio;
	}

	public void setValueRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getValueBHR() {
		return BHR;
	}

	public void setValueBHR(double bHR) {
		BHR = bHR;
	}

	public double getValueBIR() {
		return BIR;
	}

	public void setValueBIR(double bIR) {
		BIR = bIR;
	}
	
	public String toString(){
		return "Ratio: "+ratio+" BHR: "+BHR+" BIR: "+BIR;	
	}
	
//	public static Comparator<valueSet> compareByBHR
//    	= new Comparator<valueSet>() {		
//    		public int compare(valueSet v1, valueSet v2) {
//    			if(v1.getBHR()==v2.getBHR()){
//    				return 0;
//    			}else if(v1.getBHR()>v2.getBHR()){
//    				return -1;
//    			}
//    				return 1;
//    		}
//
//    	};
//    	
    public static Comparator<valueSet> compareByBIR
    	= new Comparator<valueSet>() {		
    		public int compare(valueSet v1, valueSet v2) {
    			if(v1.getValueBIR()==v2.getValueBIR()){
    				return 0;
    			}else if(v1.getValueBIR()>v2.getValueBIR()){
    				return -1;
    			}
    				return 1;
    		}

    	};
	

}
