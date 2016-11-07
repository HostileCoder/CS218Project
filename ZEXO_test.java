package CS218Project;

import java.util.ArrayList;

import org.cloudbus.cloudsim.HarddriveStorage;

public class ZEXO_test {

	public static void main(String[] args) {
		HarddriveStorage Harddrive = null;
		ArrayList <UE_Context> Evict=new ArrayList <UE_Context>();
		ArrayList<UE_Context> CacheState = new ArrayList<UE_Context>();
		ArrayList <UE_Context> requests = new ArrayList<UE_Context>();
		ArrayList <History> records = new ArrayList<History>();
		History history = null;
		
		double used=0;
		double capacity=0;
		double freespace=0;
	    double sumWeight=0;
		UE_Context file=null;
	
		Ratio ratio=new Ratio(1);

	}
	
	public static double findEXOWeight(double weight,double a, double time){
		return weight*Math.pow(Math.E, a*time);
	}

}
