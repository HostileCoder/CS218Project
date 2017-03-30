package CS218Project;

import java.text.DecimalFormat;
import java.util.TreeMap;

public class SimData {
	
	private TreeMap<Integer, Integer> Qlen = new TreeMap<Integer, Integer>();	
	private TreeMap<Double, Integer> Time = new TreeMap<Double, Integer>();		
	
	private TreeMap<Integer, Integer> QlenCDF = new TreeMap<Integer, Integer>();	
	private TreeMap<Double, Integer> TimeCDF = new TreeMap<Double, Integer>();	
	
	private double uTime = 0.000709;
	
	public void addQlen(int x){		
		if(Qlen.containsKey(x)){
			Qlen.put(x, Qlen.get(x) + 1);			
		}else{
			Qlen.put(x, 1);
		}		
	}
	
	
	public void addTime(double x){			
		x=format(x);
		if(Time.containsKey(x)){
			Time.put(x, Time.get(x) + 1);			
		}else{
			Time.put(x, 1);
		}		
	}
	
	public double format(double x){
		int n = (int)Math.ceil(x/uTime);
		return n*uTime;		
	}
	
	
	public String printPDF(){
		DecimalFormat df = new DecimalFormat("0.000000");
		String x="";

		for(int i:Qlen.keySet()){
			x=x.concat("\n");
			//x=x.concat(i+"	"+Qlen.get(i));		
			x=x.concat(""+Qlen.get(i));	
		}
		
		x=x.concat("\n");
		x=x.concat("\n");
		x=x.concat("\n");
		
		for(double i:Time.keySet()){
			x=x.concat("\n");
			//x=x.concat(df.format(i)+"	"+Time.get(i));		
			x=x.concat(""+Time.get(i));	
		}
		x=x.concat("\n");
		x=x.concat(sumQ()+" "+sumT());
		return x;
	}	
	
	
	
	public String printCDF(){
		QlenCDF();
		TimeCDF();
		
		String x="";
		for(int i:QlenCDF.keySet()){
			x=x.concat("\n");	
			x=x.concat(""+QlenCDF.get(i));	
		}
		
		x=x.concat("\n");
		x=x.concat("\n");
		x=x.concat("\n");
		
		for(double i:TimeCDF.keySet()){
			x=x.concat("\n");
			x=x.concat(""+TimeCDF.get(i));	
		}
		x=x.concat("\n");
		x=x.concat(sumQ()+" "+sumT());
		return x;
	}
	
	
	public void QlenCDF(){
		int sum=0;
		for(int x : Qlen.keySet()){
			sum=Qlen.get(x)+sum;
			QlenCDF.put(x, sum);
		}
	}
	
	
	public void TimeCDF(){
		int sum=0;
		for(double x : Time.keySet()){
			sum=Time.get(x)+sum;
			TimeCDF.put(x, sum);
		}
	}
	
	public int sumQ(){
		int x=0;
		for(int i:Qlen.values()){
			x=x+i;
		}
		return x;
	}
	
	public int sumT(){
		int x=0;
		for(int i:Time.values()){
			x=x+i;
		}
		return x;
	}
}






//public void addQlen(int x, int id){
//	TreeMap<Integer, Integer> Qlen=null;	
//	if(id==0){
//		Qlen=Qlen0;
//	}else if(id==1){
//		Qlen=Qlen1;
//	}else if(id==2){
//		Qlen=Qlen2;
//	}else if(id==3){
//		Qlen=Qlen3;
//	}
//		
//	if(Qlen.containsKey(x)){
//		Qlen.put(x, Qlen.get(x) + 1);			
//	}else{
//		Qlen.put(x, 1);
//	}		
//}
//
//
//public void addTime(double x, int id){
//	TreeMap<Double, Integer> Time=null;	
//	if(id==0){
//		Time=Time0;
//	}else if(id==1){
//		Time=Time1;
//	}else if(id==2){
//		Time=Time2;
//	}else if(id==3){
//		Time=Time3;
//	}
//			
//	x=format(x);
//	if(Time.containsKey(x)){
//		Time.put(x, Time.get(x) + 1);			
//	}else{
//		Time.put(x, 1);
//	}		
//}
//
//public String printResult(){
//	DecimalFormat df = new DecimalFormat("0.000000");
//	String x="";
//	for(int i:Qlen0.keySet()){
//		x=x.concat(i+"	"+Qlen0.get(i));
//		x=x.concat("	"+Qlen1.get(i));
//		x=x.concat("	"+Qlen2.get(i));
//		x=x.concat("	"+Qlen3.get(i));
//	
//		x=x.concat("\n");
//	}
//	x=x.concat("\n");
//	return x;
//}

//private TreeMap<Double, Integer> Time0 = new TreeMap<Double, Integer>();
//private TreeMap<Double, Integer> Time1 = new TreeMap<Double, Integer>();
//private TreeMap<Double, Integer> Time2 = new TreeMap<Double, Integer>();
//private TreeMap<Double, Integer> Time3 = new TreeMap<Double, Integer>();

//private TreeMap<Integer, Integer> Qlen0 = new TreeMap<Integer, Integer>();
//private TreeMap<Integer, Integer> Qlen1 = new TreeMap<Integer, Integer>();
//private TreeMap<Integer, Integer> Qlen2 = new TreeMap<Integer, Integer>();
//private TreeMap<Integer, Integer> Qlen3 = new TreeMap<Integer, Integer>();






