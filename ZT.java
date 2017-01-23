package CS218Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.core.CloudSim;

public class ZT {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CloudSim.init(1, Calendar.getInstance(), false);
//		AHP ahp=new AHP(4);
//		ahp.setWeight(0,1,5);
//		ahp.setWeight(0,2,5);
//		ahp.setWeight(0,3,5);
//		ahp.setWeight(1,2,5);
//		ahp.setWeight(1,3,10);
//		ahp.setWeight(2,3,10);
//		ahp.findWeight();
//		System.out.println(ahp.getResult()[0]+" "+ahp.getResult()[1]+" "+ahp.getResult()[2]+" "+ahp.getResult()[3]);
		
		VRAM x=new VRAM(100,0);
		//100 97 3
		for(int i=0;i<97;i++)
			x.addFile(new File(Integer.toString(i), 100));
		//100 96 0
		x.removeSpace(4);
		System.out.println(x);
		x.printContent();
		System.out.println(x);
		//102 96 6
		x.addSpace(6);
		System.out.println(x);
		x.printContent();
		//102 97 5
		x.addFile(new File("gh", 100));
		//102 96 6
		x.removeFile(x.ram.get(0));
		//100 96 4
		x.removeSpace(2);
		//100 100 0
		x.addFile(new File("gh0", 100));
		x.addFile(new File("gh1", 100));
		x.addFile(new File("gh2", 100));
		x.addFile(new File("gh3", 100));
		//101 100 1
		x.addSpace(1);
		//101 101 0
		x.addFile(new File("gh4", 100));
		x.printContent();	
		System.out.println(x);
	
	}

}
