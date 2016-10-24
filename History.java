package CS218Project;

public class History {
	private double totalHit=0.0;
	private double numInsert=0.0;
	private double totalRequest=0.0;
	private double BHR=0.0;
	private double BIR=0.0;
	private double varRatio=1.0;
	private double preBHR=0.0;
	private double preBIR=0.0;
	
	public History(double totalHit ,double numInsert,double totalRequest,double varRatio){
		this.totalHit=totalHit;
		this.totalRequest=totalRequest;
		this.numInsert=numInsert;
		this.varRatio=varRatio;
	}
	
	
	public double getTotalHit() {
		return totalHit;
	}
	public void incTotalHit() {
		totalHit = totalHit+1;
		setPreBHR(BHR);
		setPreBIR(BIR);
		setBHR(totalHit/totalRequest);
		setBIR(numInsert/totalRequest);
	}
	public double getNumInsert() {
		return numInsert;
	}
	public void incNumInsert() {
		numInsert = numInsert+1;
		setPreBHR(BHR);
		setPreBIR(BIR);
		setBHR(totalHit/totalRequest);
		setBIR(numInsert/totalRequest);
	}
	public double getTotalRequest() {
		return totalRequest;
	}
	public void incTotalRequest() {
		totalRequest = totalRequest+1;
		setPreBHR(BHR);
		setPreBIR(BIR);
		setBHR(totalHit/totalRequest);
		setBIR(numInsert/totalRequest);
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


	public double getVarRatio() {
		return varRatio;
	}


	public void setVarRatio(double varRatio) {
		this.varRatio = varRatio;
	}


	public double getPreBHR() {
		return preBHR;
	}


	public void setPreBHR(double preBHR) {
		this.preBHR = preBHR;
	}


	public double getPreBIR() {
		return preBIR;
	}


	public void setPreBIR(double preBIR) {
		this.preBIR = preBIR;
	}
	
}
