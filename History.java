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
	
	public  double HP=0;
	public  double LP=0;
	public  double HB=0;
	public  double LB=0;
	
	public  double HPH=0;
	public  double LPH=0;
	public  double HBH=0;
	public  double LBH=0;
	
	public History(double totalHit ,double numInsert,double totalRequest,double varRatio){
		this.totalHit=totalHit;
		this.totalRequest=totalRequest;
		this.numInsert=numInsert;
		this.varRatio=varRatio;
	}
	
	
	public double getTotalHit() {
		return totalHit;
	}

	public double getNumInsert() {
		return numInsert;
	}
	
	

	public double getBHR() {
		//return totalHit/totalRequest;
		return BHR;
	}


	public void setBHR(double bHR) {
		BHR = bHR;
	}


	public double getBIR() {
		//return numInsert/totalRequest;
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
	
	public String toString(){
		return "hit: "+totalHit +" Insert: "+ numInsert +" request: "+ totalRequest +" ratio: "+ varRatio+" BHR: "+ BHR+" BIR: "+ BIR;	
	}
	
	
	public void incTotalHit() {
		totalHit = totalHit+1;
		setPreBHR(BHR);
		setPreBIR(BIR);
		setBHR(totalHit/totalRequest);
		setBIR(numInsert/totalRequest);
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
	
	public void incCriteria(int c,int r){
		if(c==0){
			HP++;
			if(r==1)
				HPH++;
		}else if(c==1){
			LP++;
			if(r==1)
				LPH++;
		}else if(c==2){
			HB++;
			if(r==1)
				HBH++;
		}else if(c==3){
			LB++;
			if(r==1)
				LBH++;
		}
	}
	
	
}
