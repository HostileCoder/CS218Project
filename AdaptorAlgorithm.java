package CS218Project;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cloudbus.cloudsim.Log;



class AdaptorAlgorithm implements Runnable{
	//private History history = null;
	private int eventNum=0;
	private int eventCap=3;
	private int groupSize=0;
	private double localratio=0.0;
	private double localBHR=0.0;
	private double localBIR=0.0;
	private double localpreBHR=0.0;
	private double localpreBIR=0.0;
	private ArrayList <History> records = new ArrayList<History>();
	private Ratio ratio=null;
	
	private TreeMap<Double, Group> groups = new TreeMap<Double, Group>();
	private Map<Double,valueSet> candidateValue = new  TreeMap<Double,valueSet>();
	
	public AdaptorAlgorithm(int groupSize, ArrayList <History> records, Ratio ratio){
		this.groupSize = groupSize;
		this.records = records;
		this.ratio=ratio;
		double threshold = 1.0/(double) groupSize;
		double name=0.0;
		
		for(int i=0;i<groupSize;i++){
			Group g= new Group(Double.toString(name));
			groups.put(name, g);
			name=name+threshold;
			name=Math.round(name*10.0)/10.0;
		}

		//ratio
		for(int i=1;i<100;i++){
			candidateValue.put( (double)i, new valueSet((double)i)  );
		}
		
		for(valueSet e:candidateValue.values() ){
			group(e);
		}	
		
	
//		ArrayList<Group> x = new ArrayList<Group>();
//		for(Group g:groups.values()){
//			x.add(g);
//		}
//		x.get(3).setRep(4.5);
//		Collections.sort(x, Group.compareByRep);
//		System.out.println(x.get(0));
		
//		for(Group g: groups.values()){
//			System.out.println("Group "+g.getName()+" size:"+ g.getValueSetList().size());
//			for(valueSet v: g.getValueSetList().values()){
//				System.out.println(v);
//			}
//		}
	}

	
	
	@Override
	public void run() {

		while(true)
		{	
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while(! records.isEmpty()){
				body();
			}
		}
	}
	
	
	public void body(){
				
		History history = records.remove(0);
		System.out.println("Adopt: "+history);
		
		eventNum++;				
		valueSet x=candidateValue.get(history.getVarRatio());
		x.setValueBHR(history.getBHR());
		x.setValueBIR(history.getBIR());
		
		if(eventNum==eventCap){
			eventNum=0;			
			x.setValueBHR(( history.getBHR() + history.getPreBHR() )/2      );
			x.setValueBIR(( history.getBIR() + history.getPreBIR() )/2      );
			
			for(Group g: groups.values()){
				if(g.getValueSetList().containsKey(x.getValueRatio())){
					g.getValueSetList().remove(x.getValueRatio());
				}
			}			
			group(x);	
			
			//group with largest representative value (BHR)
			double max=0.0;
			Group selected = null;
			for(Group g: groups.values()){
				if(g.getRep()>=max){
					max=g.getRep();
					selected = g;
				}
			}
			
			//ratio with smallest BIR in the group
			double newRatio;
			if(selected.valueSetWithSmallestBIR()==null){
				newRatio=ratio.getRatio();
			}else
				newRatio=selected.valueSetWithSmallestBIR().getValueRatio();
				
			Log.printLine("");	
			Log.printLine("Adopt	group selected:"+selected);	
			Log.printLine("Adopt	New Ratio:"+newRatio);
			Log.printLine("	BHR: "+history.getBHR()+" PreBHR "+history.getPreBHR());	
			Log.printLine("	BIR: "+history.getBIR()+" PreBIR "+history.getPreBIR());							
			for(valueSet v:selected.getValueSetList().values()){
				Log.printLine(v);
			}
			
			ratio.setRatio(newRatio);	
			
		
		}
					
	}
	
	public void group(valueSet v){
		double x = v.getValueBHR();
		if(x<=0.1){
			Group g = groups.get(0.0);
			g.addvalue(v);
			Log.printLine("insert at group 0.0");
		}else if(x>0.1 && x<=0.2){
			Group g = groups.get(0.1);
			g.addvalue(v);
			Log.printLine("insert at group 0.1");
		}else if(x>0.2 && x<=0.3){
			Group g = groups.get(0.2);
			g.addvalue(v);
			Log.printLine("insert at group 0.2");
		}else if(x>0.3 && x<=0.4){
			Group g = groups.get(0.3);
			g.addvalue(v);
			Log.printLine("insert at group 0.3");
		}else if(x>0.4 && x<=0.5){
			Group g = groups.get(0.4);
			g.addvalue(v);
			Log.printLine("insert at group 0.4");
		}else if(x>0.5 && x<=0.6){
			Group g = groups.get(0.5);
			g.addvalue(v);
			Log.printLine("insert at group 0.5");
		}else if(x>0.6 && x<=0.7){
			Group g = groups.get(0.6);
			g.addvalue(v);
			Log.printLine("insert at group 0.6");
		}else if(x>0.7 && x<=0.8){
			Group g = groups.get(0.7);
			g.addvalue(v);
			Log.printLine("insert at group 0.7");
		}else if(x>0.8 && x<=0.9){
			Group g = groups.get(0.8);
			g.addvalue(v);
			Log.printLine("insert at group 0.8");
		}else if(x>0.9){
			Group g = groups.get(0.9);
			g.addvalue(v);
			Log.printLine("insert at group 0.9");
		}		
	}
	
	public Map<Double, Group> getGroups(){
		return groups;
	}



	public int getGroupSize() {
		return groupSize;
	}



	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}



	public double getLocalratio() {
		return localratio;
	}



	public void setLocalratio(double localratio) {
		this.localratio = localratio;
	}



	public double getLocalBHR() {
		return localBHR;
	}



	public void setLocalBHR(double localBHR) {
		this.localBHR = localBHR;
	}



	public double getLocalBIR() {
		return localBIR;
	}



	public void setLocalBIR(double localBIR) {
		this.localBIR = localBIR;
	}



	public double getLocalpreBHR() {
		return localpreBHR;
	}



	public void setLocalpreBHR(double localpreBHR) {
		this.localpreBHR = localpreBHR;
	}



	public double getLocalpreBIR() {
		return localpreBIR;
	}



	public void setLocalpreBIR(double localpreBIR) {
		this.localpreBIR = localpreBIR;
	}
	
	

}