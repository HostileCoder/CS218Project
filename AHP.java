package CS218Project;

import org.ejml.simple.SimpleMatrix;

public class AHP {
	private int size=0;
	private SimpleMatrix mx;
	private double[] result ;
	public AHP(int size){
		this.size=size;
		this.mx=new SimpleMatrix(size,size);
		result = new double[size];
		for(int i=0;i<size;i++){
			mx.set(i, i, 1);	  
		}
	}
	
	public SimpleMatrix getMX(){
		return mx;
	}
	
	
	public double[] findWeight(){
		
		double[] rowWeight= new double[this.size];
		double[] OldrowWeight = new double[this.size];
		double sum=mx.elementSum();		
		
		while(true){
			SimpleMatrix tmx=mx.mult(mx);
			sum=tmx.elementSum();		
			for(int i=0;i<this.size;i++){
				for(int j=0;j<this.size;j++){
					rowWeight[i]=rowWeight[i]+tmx.get(i, j);
				}				
				rowWeight[i]=rowWeight[i]/sum;
				rowWeight[i]=Math.round(rowWeight[i]*1000.0)/1000.0;
			}
	
			if(rowWeight[0]==OldrowWeight[0]){
				break;
			}
				
			OldrowWeight=rowWeight;
		}
		
		this.setResult(rowWeight);
		return getResult();
	}
	
	
	
	public void setWeight(int i, int j, double weight ) { 		   	   
		if (i != j) { 
			mx.set(i, j, weight);	  
			mx.set(j, i, 1.0/weight);
	    } 
	} 
	
		
	public double[] getResult() {
		return result;
	}

	public void setResult(double[] result) {
		this.result = result;
	}
	
	public String toString(){
		String x="";
		for(int i=0;i<getResult().length;i++){
			x=x.concat(""+getResult()[i]+"\n");
		}
		return x;
	}
	
	
//	private void fillMx(double[] weight,int size, SimpleMatrix mx )
//	{ 
//	    int k=0;        
//	    for(int i=0; i<size; i++)
//	    {
//	    	k=0;
//	        for(int j=0; j<size;j++)
//	        {
//	            if(i==j)
//	          		mx.set(i, j, 1);
//	            else if(i<j)
//	            {
//	                mx.set(i, j,  weight[k]);
//	                k++;
//	            }
//	            else if(i>j)
//	            	mx.set(i, j, 1/mx.get(j,i));
//	        }
//	    }
//	}
	
	
//	public void setWeight(int row,int colume ,double w){
//	if(w<=0.0){
//		System.out.println("illegal weight");
//		return;
//	}else if(row>=size || colume>=size){
//		System.out.println("illegal index");
//		return;
//	}	
//		weight[index]=w;
//}
}


