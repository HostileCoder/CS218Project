package CS218Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;

public class ZT {

	public static void main(String[] args) throws ParameterException {
		// TODO Auto-generated method stub
		CloudSim.init(1, Calendar.getInstance(), false);
		AHP ahp=new AHP(4);
		ahp.setWeight(0,1,10);
		ahp.setWeight(0,2,10);
		ahp.setWeight(0,3,10);
		ahp.setWeight(1,2,10);
		ahp.setWeight(1,3,10);
		ahp.setWeight(2,3,10);
		ahp.findWeight();
		System.out.println(ahp.getResult()[0]+" "+ahp.getResult()[1]+" "+ahp.getResult()[2]+" "+ahp.getResult()[3]);
		
		
//		for(double i=-1;i<=1.2;i=i+0.4){
//			double a=Math.pow(10, i);
//			a=Math.round(a*100.0);
//			a=a/100.0;
//			System.out.println(a);
//		}
//		
//		Random rn = new Random();
//		for(int i=0;i<122;i++){
//			int max=3;
//			int min=0;
//			int ran= rn.nextInt(max - min + 1) + min;
//			System.out.println(ran);
//		}
		
		
		
		ArrayList<UE_Context> v=  new 	ArrayList<UE_Context>();
		v.add(new UE_Context(Integer.toString(1),1,0.0,1,1));
		v.add(new UE_Context(Integer.toString(2),2,0.0,1,1));
		v.add(new UE_Context(Integer.toString(3),3,0.0,1,1));
		
		
		v.get(0).setRatio(5);
		v.get(1).setRatio(2);
		v.get(2).setRatio(3);
		Collections.sort(v);
		
		for(UE_Context u:v)
			System.out.println(u.getName());
			
	}

}
