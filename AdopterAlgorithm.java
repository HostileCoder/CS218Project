package CS218Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Host;



class AdopterAlgorithm implements Runnable{
	private History history = null;
	private boolean hit;
	private boolean inserted;
	private boolean ratio;
	private int eventNum=0;
	private int eventCap=10;
	private Map<Double, BHR_Group> groups = new HashMap<Double, BHR_Group>();
	private int groupSize=0;
	//private ArrayList<valueSet> candidateValue = new  ArrayList<valueSet>();
	private Map<Double,valueSet> candidateValue = new  HashMap<Double,valueSet>();
	
	public AdopterAlgorithm(int groupSize, History history){
		this.groupSize = groupSize;
		this.history = history;
		double threshold = 1.0/(double) groupSize;
		double name=0.0;
		
		for(int i=0;i<groupSize;i++){
			BHR_Group g= new BHR_Group(Double.toString(name));
			groups.put(name, g);
			name=name+threshold;
			name=Math.round(name*10.0)/10.0;
		}
			
		
		for(int i=1;i<100;i++){
			//candidateValue.add(new valueSet((double)i));
			candidateValue.put( (double)i, new valueSet((double)i)  );
		}
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void body(){
		eventNum++;		
		
		valueSet x=candidateValue.get(history.getVarRatio());
		x.setBHR(history.getBHR());
		x.setBIR(history.getBIR());
		
		if(eventNum==eventCap){
			eventNum=0;
			
			x.setBHR( (history.getBHR()+history.getPreBHR())/2      );
			x.setBIR( (history.getBIR()+history.getPreBIR())/2      );
			
			
			
			
		}
			
	}
	
	public void group(){
		
		
		
		
	}
	
	public Map<Double, BHR_Group> getGroups(){
		return groups;
	}
	
	

}