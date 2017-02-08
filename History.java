package CS218Project;

public class History {
	private double totalMiss=0.0;
	private double BMR=0.0;
	private double totalHit=0.0;
	private double numInsert=0.0;
	private double totalRequest=0.0;
	private double BHR=0.0;
	private double BIR=0.0;
	private double varRatio=1.0;

	
//	//HL insert
//	public  double HMI=0;
//	public  double LMI=0;
//	
//	//HL hit
//	public  double HMH=0;
//	public  double LMH=0;
//	
//	//HL write
//	public  double HMW=0;
//	public  double LMW=0;
	
	public  double L1=0;
	public  double L2=0;
	public  double L3=0;
	public  double L4=0;
	
	public  double L1H=0;
	public  double L2H=0;
	public  double L3H=0;
	public  double L4H=0;
	
	public  double L1W=0;
	public  double L2W=0;
	public  double L3W=0;
	public  double L4W=0;
	
	public  double L1I=0;
	public  double L2I=0;
	public  double L3I=0;
	public  double L4I=0;
	
	public int writes=0;
	
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


	
	public String toString(){
		return "hit:"+totalHit+
				" Insert:"+numInsert +
				" Requests:"+totalRequest+
				" Writes:"+writes+
				" BHR:"+ BHR+
				" BIR:"+ BIR+
				" BMR:"+ BMR;	
	}
	
	
	public void incTotalMiss() {
		totalMiss++;
		BMR=totalMiss/totalRequest;
	}
	
	public double getTotalMiss() {
		return totalMiss;
	}
	
	public void incTotalHit() {
		totalHit = totalHit+1;
//		setPreBHR(BHR);
//		setPreBIR(BIR);
		setBHR(totalHit/totalRequest);
		setBIR(numInsert/totalRequest);
	}

	public void incNumInsert() {
		numInsert = numInsert+1;
//		setPreBHR(BHR);
//		setPreBIR(BIR);
		setBHR(totalHit/totalRequest);
		setBIR(numInsert/totalRequest);
	}
	public double getTotalRequest() {
		return totalRequest;
	}
	
	public void incTotalRequest() {
		totalRequest = totalRequest+1;
//		setPreBHR(BHR);
//		setPreBIR(BIR);
		setBHR(totalHit/totalRequest);
		setBIR(numInsert/totalRequest);
	}
	
	public void incIdvHit(int c,int r){
		if(c==0){
			L1++;
			if(r==1)
				L1H++;
		}else if(c==1){
			L2++;
			if(r==1)
				L2H++;
		}else if(c==2){
			L3++;
			if(r==1)
				L3H++;
		}else if(c==3){
			L4++;
			if(r==1)
				L4H++;
		}
	}

	public void incIdvWrite(int c,int num){
		//this.writes = writes+num;
		if(c==0){
				L1W=L1W+num;
		}else if(c==1){
				L2W=L2W+num;
		}else if(c==2){
				L3W=L3W+num;
		}else if(c==3){
				L4W=L4W+num;
		}
	}
	
	
	
	public void incIdvInsert(int c,int num){
		if(c==0){
				L1I=L1I+num;
		}else if(c==1){
				L2I=L2I+num;
		}else if(c==2){
				L3I=L3I+num;
		}else if(c==3){
				L4I=L4I+num;
		}
	}
	

	public int getWrites() {
		return writes;
	}


	public void addWrites(int v) {
		this.writes = writes+v;
	}
	
	
//	public double getPreBHR() {
//	return preBHR;
//}
//
//
//public void setPreBHR(double preBHR) {
//	this.preBHR = preBHR;
//}
//
//
//public double getPreBIR() {
//	return preBIR;
//}
//
//
//public void setPreBIR(double preBIR) {
//	this.preBIR = preBIR;
//}
	

	
//	public void incMobilityHit(int c,int num){
//	if(c==0||c==2){
//		HMH=HMH+num;
//	}else if(c==1||c==3){
//		LMH=LMH+num;
//	}
//}
//
//public void incMobilityInsert(int c,int num){
//	if(c==0||c==2){
//		HMI=HMI+num;
//	}else if(c==1||c==3){
//		LMI=LMI+num;
//	}
//}
//
//public void incMobilityWrite(int c,int num){
//	if(c==0||c==2){
//		HMW=HMW+num;
//	}else if(c==1||c==3){
//		LMW=LMW+num;
//	}
//}
	
}
