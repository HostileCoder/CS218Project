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
	private double preBHR=0.0;
	private double preBIR=0.0;
	
	
	public  double HMI=0;
	public  double LMI=0;
	
	public  double HMH=0;
	public  double LMH=0;
	
	public  double HMW=0;
	public  double LMW=0;
	
	public  double HP=0;
	public  double LP=0;
	public  double HB=0;
	public  double LB=0;
	
	public  double HPH=0;
	public  double LPH=0;
	public  double HBH=0;
	public  double LBH=0;
	
	public  double HPW=0;
	public  double LPW=0;
	public  double HBW=0;
	public  double LBW=0;
	
	public  double HPI=0;
	public  double LPI=0;
	public  double HBI=0;
	public  double LBI=0;
	
	private int writes=0;
	
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
	
	public void incIdvHit(int c,int r){
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

	public void incIdvWrite(int c,int num){
		//this.writes = writes+num;
		if(c==0){
				HPW=HPW+num;
		}else if(c==1){
				LPW=LPW+num;
		}else if(c==2){
				HBW=HBW+num;
		}else if(c==3){
				LBW=LBW+num;
		}
	}
	
	
	
	public void incIdvInsert(int c,int num){
		if(c==0){
				HPI=HPI+num;
		}else if(c==1){
				LPI=LPI+num;
		}else if(c==2){
				HBI=HBI+num;
		}else if(c==3){
				LBI=LBI+num;
		}
	}
	
	public void incMobilityHit(int c,int num){
		if(c==0||c==2){
			HMH=HMH+num;
		}else if(c==1||c==3){
			LMH=LMH+num;
		}
	}
	
	public void incMobilityInsert(int c,int num){
		if(c==0||c==2){
			HMI=HMI+num;
		}else if(c==1||c==3){
			LMI=LMI+num;
		}
	}
	
	public void incMobilityWrite(int c,int num){
		if(c==0||c==2){
			HMW=HMW+num;
		}else if(c==1||c==3){
			LMW=LMW+num;
		}
	}
		

	public int getWrites() {
		return writes;
	}


	public void addWrites(int v) {
		this.writes = writes+v;
	}
	
	
}
