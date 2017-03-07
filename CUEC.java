package CS218Project;

import java.util.Comparator;


public class CUEC implements Comparator<UE_Context> {

	@Override
	public int compare(UE_Context o1, UE_Context o2) {
		double x= o1.getRatio() -o2.getRatio();	
		if(x>0){
			return 1;
		}else if(x<0){
			return -1;
		}
		return 0;
	}

}
